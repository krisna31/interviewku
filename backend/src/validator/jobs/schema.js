const Joi = require('joi');

const GetJobsFieldResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Jobs Field Ditemukan').required(),
  data: Joi.object().keys({
    jobFields: Joi.array().items(Joi.object().keys({
      id: Joi.number().max(2147483647).default(1).required(),
      name: Joi.string().default('Bidang General').required(),
      description: Joi.string().allow(null),
      // createdAt: Joi.date().required(),
      // updatedAt: Joi.date().allow(null),
    })
      .label('Jobs Field'))
      .required(),
  }),
}).label('Get Jobs Field Response');

const GetJobsPositionResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Jobs Position Ditemukan').required(),
  data: Joi.object().keys({
    jobPositions: Joi.array().items(Joi.object().keys({
      id: Joi.number().max(2147483647).default(1).required(),
      name: Joi.string().default('Frontend').required(),
      description: Joi.string().allow(null),
      // createdAt: Joi.date().required(),
      // updatedAt: Joi.date().allow(null),
    })
      .label('Jobs Positions'))
      .required(),
  }),
}).label('Get Jobs Position Response');

module.exports = {
  GetJobsFieldResponseSchema,
  GetJobsPositionResponseSchema,
};
