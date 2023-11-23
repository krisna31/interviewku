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
  jobPositionName: Joi.string().default('Backend Developer').required(),
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
    score: Joi.number().max(2147483647).allow(null).required(),
    totalDuration: Joi.number().max(2147483647).allow(null).required(),
    feedback: Joi.string().required(),
    answers: Joi.array().items(Joi.object({
      question: Joi.string().required(),
      jobPositionName: Joi.string().allow(null).required(),
      jobFieldName: Joi.string().required(),
      questionOrder: Joi.number().max(2147483647).required(),
      userAnswer: Joi.string().allow(null).required(),
      audioUrl: Joi.string().allow(null).required(),
      score: Joi.number().max(2147483647).allow(null).required(),
      duration: Joi.number().max(2147483647).allow(null).required(),
      retryAttempt: Joi.number().max(2147483647).allow(null).required(),
      strukturScore: Joi.number().max(2147483647).allow(null).required(),
      feedback: Joi.string().required(),
    })),
  }),
}).label('interviewDataResponseSchema');

module.exports = {
  PostAnswerParamsSchema,
  PostAnswerResponseSchema,
  PostAnswersPayloadByInterviewIdSchema,
  PutInterviewPayloadSchema,
  InterviewDataResponseSchema,
};
