const Joi = require('joi');

const GetQuestionsResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Answers Ditemukan').required(),
  data: Joi.object().keys({
    interviewId: Joi.string().default('sesi-interview-xxx').required(),
    questions: Joi.array().items(Joi.object({
      questionOrder: Joi.number().max(2147483647).default(1).required(),
      jobFieldName: Joi.string().default('Kuliner dan Restoran').required(),
      jobDescription: Joi.string().default('Job field related to culinary and restaurant management').required(),
      question: Joi.string().default('Apa pengalaman Anda dalam merancang dan mengelola menu restoran?').required(),
    }))
      .required(),
  }),
}).label('Get Jobs Field Response');

const GetQuestionsQuerySchema = Joi.object({
  mode: Joi.string().default('latihan').valid('latihan', 'interview').required(),
}).label('Get Jobs Field Query');

module.exports = {
  GetQuestionsResponseSchema,
  GetQuestionsQuerySchema,
};
