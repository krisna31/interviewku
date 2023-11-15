// Import library module
const Hapi = require('@hapi/hapi');
const Jwt = require('@hapi/jwt');
const Inert = require('@hapi/inert');
const Vision = require('@hapi/vision');
const HapiSwagger = require('hapi-swagger');
const Pack = require('../package.json');

// import user created module
const ClientError = require('./exceptions/ClientError');
const UsersService = require('./services/postgres/UsersService');
const AuthenticationsService = require('./services/postgres/AuthenticationsService');
const users = require('./api/users');
const jobs = require('./api/jobs');
const questions = require('./api/questions');
const answers = require('./api/answers');
const interviews = require('./api/interviews');

const authentications = require('./api/authentication');
const TokenManager = require('./tokenize/TokenManager');
const JobsService = require('./services/postgres/JobsService');
const QuestionsService = require('./services/postgres/QuestionsService');
const AnswersService = require('./services/postgres/AnswersService');
const InterviewsService = require('./services/postgres/InterviewsService');
const StorageService = require('./services/storage/storageService');
const MachineLearningService = require('./services/tensorflow/machineLearningService');

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
    },
  };

  await server.register([
    {
      plugin: Jwt,
    },
    Inert,
    Vision,
    {
      plugin: HapiSwagger,
      options: swaggerOptions,
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
      },
    },
    {
      plugin: jobs,
      options: {
        jobsService,
      },
    },
    {
      plugin: questions,
      options: {
        questionsService,
        interviewsService,
      },
    },
    {
      plugin: answers,
      options: {
        answersService,
      },
    },
    {
      plugin: interviews,
      options: {
        interviewsService,
        storageService,
        machineLearningService,
      },
    },
  ]);

  // extension function before response check and handle error
  server.ext('onPreResponse', (request, h) => {
    const { response } = request;
    console.log(response.message);

    if (response?.output?.statusCode === 401) {
      if (process.env.APP_ENV === 'dev') {
        return h.response({
          success: false,
          message: `Unauthorized - ${response.message}`,
        }).code(401);
      }
      return h.response({
        success: false,
        message: 'Unauthorized',
      }).code(401);
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
      const newResponse = h.response({
        success: false,
        message: 'terjadi kegagalan pada server kami',
      });
      newResponse.code(500);
      return newResponse;
    }
    return h.continue;
  });

  await server.start();
  console.log(`Server Berjalan di ${server.info.uri}`);
})();
