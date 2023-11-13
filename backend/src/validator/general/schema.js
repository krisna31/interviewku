const Joi = require('joi');

const WithTokenRequestSchema = Joi.object({
  authorization: Joi.string().default('Bearer xxx').required(),
}).label('Request With Access Token').unknown(true);

module.exports = {
  WithTokenRequestSchema,
};
