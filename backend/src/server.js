// Import library module
const Hapi = require('@hapi/hapi');
const Jwt = require('@hapi/jwt');
const Inert = require('@hapi/inert');
const Vision = require('@hapi/vision');
const HapiSwagger = require('hapi-swagger');
const HapiRateLimit = require('hapi-rate-limit');
const Pack = require('../package.json');

// import user created module
const ClientError = require('./exceptions/ClientError');
const UsersService = require('./services/postgres/UsersService');
const AuthenticationsService = require('./services/postgres/AuthenticationsService');
const users = require('./api/users');
const jobs = require('./api/jobs');
// const questions = require('./api/questions');
const answers = require('./api/answers');
const interviews = require('./api/interviews');
const articles = require('./api/articles');
const healthCheck = require('./api/health-check');
const chats = require('./api/chats');

const authentications = require('./api/authentication');
const TokenManager = require('./tokenize/TokenManager');
const JobsService = require('./services/postgres/JobsService');
const QuestionsService = require('./services/postgres/QuestionsService');
const AnswersService = require('./services/postgres/AnswersService');
const ArticlesService = require('./services/postgres/ArticlesService');
const InterviewsService = require('./services/postgres/InterviewsService');
const StorageService = require('./services/storage/StorageService');
const MachineLearningService = require('./services/tensorflow/MachineLearningService');
const AudioService = require('./services/audio/AudioService');
const { sendCustomResponseByStatusCode } = require('./utils');
const MailService = require('./services/email/MailService');
const HealthCheckService = require('./services/health-check/HealthCheckService');
const ChatsService = require('./services/postgres/ChatsService');

// initialize dotenv
require('dotenv').config();

(async () => {
  const usersService = new UsersService();
  const jobsService = new JobsService();
  const authenticationsService = new AuthenticationsService();
  const questionsService = new QuestionsService();
  const answersService = new AnswersService();
  const interviewsService = new InterviewsService();
  const storageService = new StorageService();
  const machineLearningService = new MachineLearningService();
  const audioService = new AudioService();
  const mailService = new MailService();
  const articlesService = new ArticlesService();
  const healthCheckService = new HealthCheckService();
  const chatsService = new ChatsService();

  const server = Hapi.server({
    port: process.env.PORT,
    host: process.env.HOST,
    routes: {
      cors: true,
    },
  });

  const swaggerOptions = {
    info: {
      title: 'InterviewKu API Documentation',
      version: Pack.version,
      description: 'Documentation for InterviewKu API',
      contact: {
        name: 'InterviewKu',
        email: 'krisnaaaputraaa@gmail.com',
      },
      license: {
        name: 'GNU Affero General Public License v3.0',
        url: 'https://github.com/krisna31/interviewku/blob/master/LICENSE',
      },
    },
    schemes: ['https', 'http'],
    // documentationPath: '/interviewku-api-docs',
    // swaggerUIPath: '/interviewku-swaggerui/',
    // sortTags: 'unsorted',
    // documentationPage: false,
  };

  const hapiRateLimitOptions = {
    userLimit: 200,
    userCache: {
      expiresIn: 60000,
    },
    // pathLimit: 50,
    // pathCache: {
    //   expiresIn: 60000,
    // },
    // userWhitelist: ['interviewku'],
    // authToken: 'jwt',
  };

  await server.register([
    {
      plugin: Jwt,
    },
  ]);

  server.auth.strategy('interviewku_jwt', 'jwt', {
    keys: process.env.ACCESS_TOKEN_KEY,
    verify: {
      aud: false,
      iss: false,
      sub: false,
      maxAgeSec: process.env.ACCESS_TOKEN_AGE,
    },
    validate: (artifacts) => ({
      isValid: true,
      credentials: {
        id: artifacts.decoded.payload.id,
      },
    }),
  });

  await server.register([
    Inert,
    Vision,
    {
      plugin: HapiSwagger,
      options: swaggerOptions,
    },
    {
      plugin: HapiRateLimit,
      options: hapiRateLimitOptions,
    },
    {
      plugin: users,
      options: {
        usersService,
        jobsService,
      },
    },
    {
      plugin: authentications,
      options: {
        authenticationsService,
        usersService,
        tokenManager: TokenManager,
        mailService,
      },
    },
    {
      plugin: jobs,
      options: {
        jobsService,
      },
    },
    // {
    //   plugin: questions,
    //   options: {
    //     questionsService,
    //     interviewsService,
    //   },
    // },
    {
      plugin: answers,
      options: {
        answersService,
      },
    },
    {
      plugin: interviews,
      options: {
        questionsService,
        interviewsService,
        storageService,
        machineLearningService,
        audioService,
        jobsService,
        answersService,
      },
    },
    {
      plugin: articles,
      options: {
        articlesService,
      },
    },
    {
      plugin: healthCheck,
      options: {
        healthCheckService,
      },
    },
    {
      plugin: chats,
      options: {
        chatsService,
        machineLearningService,
      },
    },
  ]);

  // extension function before response check and handle error
  server.ext('onPreResponse', (request, h) => {
    const { response } = request;

    const statusCode = response?.output?.statusCode;

    if (statusCode === 401) {
      return sendCustomResponseByStatusCode(response, h, 401, 'Unauthorized');
    }

    if (statusCode === 413) {
      return sendCustomResponseByStatusCode(response, h, 413, 'Payload Too Large Than 1.5MB');
    }

    if (statusCode === 429) {
      return sendCustomResponseByStatusCode(response, h, 429, 'Rate limit exceeded Try again after 1 minute');
    }

    if (response instanceof Error) {
      if (response instanceof ClientError) {
        const newResponse = h.response({
          success: false,
          message: response.message,
        });
        newResponse.code(response.statusCode);
        return newResponse;
      }
      if (!response.isServer) {
        return h.continue;
      }

      console.error('🚀 ~ file: server.js:227 ~ server.ext ~ response:', response.message);

      const newResponse = h.response({
        success: false,
        message: process.env.APP_ENV === 'dev' ? `terjadi kegagalan pada server kami - ${response.message}` : 'terjadi kegagalan pada server kami',
      });
      newResponse.code(500);
      return newResponse;
    }
    return h.continue;
  });

  await server.start();
  console.info(`Server Berjalan di ${server.info.uri}`);
})();
