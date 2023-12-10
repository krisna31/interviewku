const Joi = require('joi');

const WithTokenRequestSchema = Joi.object({
  authorization: Joi.string().default('Bearer xxx').required(),
}).label('Request With Access Token').unknown(true);

const StatusAndMessageResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Success').required(),
}).label('Status and Message Response');

module.exports = {
  WithTokenRequestSchema,
  StatusAndMessageResponseSchema,
};
