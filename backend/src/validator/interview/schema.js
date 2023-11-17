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
    _readableState: Joi.object(),
    _events: Joi.object(),
    _eventsCount: Joi.number(),
    _maxListeners: Joi.any(),
    _data: Joi.any(),
    _position: Joi.number(),
    _encoding: Joi.string(),
    hapi: Joi.object({
      filename: Joi.string().required(),
      headers: Joi.object({
        'content-type': Joi.string().valid('audio/aac', 'audio/mpeg', 'audio/ogg', 'audio/opus', 'audio/wav', 'audio/webm', 'audio/amr').required(),
      }).unknown().required(),
    }).unknown().required(),
  },
  jobPositionName: Joi.string().default('Backend Developer').required(),
  retryAttempt: Joi.number().max(2147483647).default(0).required(),
  question: Joi.string().required(),
  questionOrder: Joi.number().max(2147483647).required(),
}).unknown().label('PostAnswersPayloadByInterviewIdSchema');

const PutInterviewPayloadSchema = Joi.object({
  completed: Joi.boolean().required(),
}).label('PutInterviewPayloadSchema');

const interviewDataResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Sesi interview berhasil ditutup').required(),
  data: Joi.object({
    interviewId: Joi.string().required(),
    mode: Joi.string().valid('latihan', 'interview').required(),
    totalQuestions: Joi.number().max(2147483647).required(),
    completed: Joi.boolean().required(),
    score: Joi.number().max(2147483647).required(),
    totalDuration: Joi.number().max(2147483647).required(),
    feedback: Joi.string().required(),
    answers: Joi.array().items(Joi.object({
      question: Joi.string().required(),
      jobPositionName: Joi.string().required(),
      jobFieldName: Joi.string().required(),
      questionOrder: Joi.number().max(2147483647).required(),
      userAnswer: Joi.string().required(),
      audioUrl: Joi.string().required(),
      score: Joi.number().max(2147483647).required(),
      duration: Joi.number().max(2147483647).required(),
      retryAttempt: Joi.number().max(2147483647).required(),
      strukturScore: Joi.number().max(2147483647).required(),
      feedback: Joi.string().required(),
    })),
  }),
}).label('interviewDataResponseSchema');

module.exports = {
  PostAnswerParamsSchema,
  PostAnswerResponseSchema,
  PostAnswersPayloadByInterviewIdSchema,
  PutInterviewPayloadSchema,
  interviewDataResponseSchema,
};
