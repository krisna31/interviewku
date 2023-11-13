const Joi = require('joi');

const GetQuestionsResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Answers Ditemukan').required(),
  data: Joi.object().keys({
    questions: Joi.array().items(Joi.object({
      id: Joi.number().required(),
      jobFieldName: Joi.string().default('Kuliner dan Restoran').required(),
      jobDescription: Joi.string().default('Job field related to culinary and restaurant management').required(),
      question: Joi.string().default('Apa pengalaman Anda dalam merancang dan mengelola menu restoran?').required(),
    }))
      .required(),
  }),
}).label('Get Jobs Field Response');

module.exports = {
  GetQuestionsResponseSchema,
};
