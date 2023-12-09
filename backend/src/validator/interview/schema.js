const Joi = require('joi');

const PostAnswerParamsSchema = Joi.object({
  interviewId: Joi.string().required(),
}).label('PostAnswerParamsSchema');

const PostAnswerResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Jawaban berhasil disimpan').required(),
  data: Joi.object({
    testHistoryId: Joi.string(),
  }),
}).label('PostAnswerResponseSchema');

const PostAnswersPayloadByInterviewIdSchema = Joi.object({
  // audio: Joi.any().meta({ swaggerType: 'file' }).required(),
  audio: {
    bytes: Joi.number().required(),
    filename: Joi.string().required(),
    path: Joi.string().required(),
    headers: Joi.object({
      'content-type': Joi.string().valid('audio/aac', 'audio/mpeg', 'audio/ogg', 'audio/opus', 'audio/wav', 'audio/webm', 'audio/amr').required(),
    }).unknown().required(),
  },
  token: Joi.string().default('x*32').length(32).required(),
  retryAttempt: Joi.number().max(2147483647).default(0).required(),
  question: Joi.string().required(),
  questionOrder: Joi.number().max(2147483647).required(),
}).unknown().label('PostAnswersPayloadByInterviewIdSchema');

const PutInterviewPayloadSchema = Joi.object({
  completed: Joi.boolean().required(),
  token: Joi.string().default('x*32').length(32).required(),
}).label('PutInterviewPayloadSchema');

const InterviewDataResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Sesi interview berhasil ditutup').required(),
  data: Joi.object({
    interviewId: Joi.string().required(),
    mode: Joi.string().valid('latihan', 'interview').required(),
    totalQuestions: Joi.number().max(2147483647).required(),
    completed: Joi.boolean().required(),
    score: Joi.number().min(1).max(5).allow(null)
      .required(),
    totalDuration: Joi.number().max(2147483647).allow(null).required(),
    feedback: Joi.string().required(),
    jobFieldName: Joi.string().required(),
    startedAt: Joi.date().required(),
    answers: Joi.array().items(Joi.object({
      question: Joi.string().required(),
      jobFieldName: Joi.string().required(),
      questionOrder: Joi.number().max(2147483647).required(),
      userAnswer: Joi.string().allow(null).allow('').required(),
      audioUrl: Joi.string().allow(null).required(),
      score: Joi.number().min(1).max(5).allow(null)
        .required(),
      duration: Joi.number().max(2147483647).allow(null).required(),
      retryAttempt: Joi.number().max(2147483647).allow(null).required(),
      // strukturFeedback: Joi.string().allow(null).required(),
      feedback: Joi.string().required(),
    })),
  }),
}).label('interviewDataResponseSchema');

const GetQuestionsResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Answers Ditemukan').required(),
  data: Joi.object().keys({
    interviewId: Joi.string().default('sesi-interview-xxx').required(),
    token: Joi.string().default('x*32').length(32),
    questions: Joi.array().items(Joi.object({
      questionOrder: Joi.number().max(2147483647).default(1).required(),
      jobFieldName: Joi.string().default('Kuliner dan Restoran').required(),
      // eslint-disable-next-line max-len
      // jobDescription: Joi.string().default('Job field related to culinary and restaurant management').required(),
      question: Joi.string().default('Apa pengalaman Anda dalam merancang dan mengelola menu restoran?').required(),
    }))
      .required(),
  }),
}).label('Get Jobs Field Response');

const GetQuestionsQuerySchema = Joi.object({
  mode: Joi.string().default('latihan').valid('latihan', 'interview').required(),
  jobFieldId: Joi.number().min(6).max(2147483647).default(1)
    .required(),
}).label('Get Jobs Field Query');

const ListOfInterviewDataResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Sesi interview berhasil ditutup').required(),
  meta: Joi.object().keys({
    count: Joi.number().default(1).required(),
    currentPage: Joi.number().default(1).required(),
    totalData: Joi.number().default(1).required(),
    nextUrl: Joi.string().allow(null),
    previousUrl: Joi.string().allow(null),
    firstPageUrl: Joi.string().required(),
    lastPageUrl: Joi.string().required(),
    limit: Joi.number().default(10).required(),
  })
    .label('Meta')
    .required(),
  data: Joi.array().items(Joi.object({
    interviewId: Joi.string().required(),
    mode: Joi.string().valid('latihan', 'interview').required(),
    totalQuestions: Joi.number().max(2147483647).required(),
    completed: Joi.boolean().required(),
    score: Joi.number().min(1).max(5).allow(null)
      .required(),
    totalDuration: Joi.number().max(2147483647).allow(null).required(),
    feedback: Joi.string().required(),
    startedAt: Joi.date().required(),
    jobFieldName: Joi.string().required(),
  })),
}).label('List Of Interview History Response');

module.exports = {
  PostAnswerParamsSchema,
  PostAnswerResponseSchema,
  PostAnswersPayloadByInterviewIdSchema,
  PutInterviewPayloadSchema,
  InterviewDataResponseSchema,
  GetQuestionsQuerySchema,
  GetQuestionsResponseSchema,
  ListOfInterviewDataResponseSchema,
};
