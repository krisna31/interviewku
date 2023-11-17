const Joi = require('joi');

const GetAnwersResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Answers Ditemukan').required(),
  data: Joi.object().keys({
    question: Joi.string().default('Apa pengalaman Anda dalam merancang dan mengelola menu restoran?').required(),
    answers: Joi.array().items(Joi.object({
      answer: Joi.string().default('...').required(),
    }))
      .required(),
  }),
}).label('Get Jobs Field Response');

const GetAnwersRequestParamsSchema = Joi.object({
  questionId: Joi.number().max(2147483647).required(),
}).label('Get Jobs Field Request Params');

module.exports = {
  GetAnwersResponseSchema,
  GetAnwersRequestParamsSchema,
};
