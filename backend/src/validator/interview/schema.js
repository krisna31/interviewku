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
  jobFieldName: Joi.string().default('IT').required(),
  jobPositionName: Joi.string().default('Backend Developer').required(),
  retryAttempt: Joi.number().default(0).required(),
  question: Joi.string().required(),
}).unknown().label('PostAnswersPayloadByInterviewIdSchema');

module.exports = {
  PostAnswerParamsSchema,
  PostAnswerResponseSchema,
  PostAnswersPayloadByInterviewIdSchema,
};
