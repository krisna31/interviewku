const Joi = require('joi');

const WithTokenRequestSchema = Joi.object({
  authorization: Joi.string().required().default('Bearer xxx'),
}).label('Request With Access Token').unknown(true);

module.exports = {
  WithTokenRequestSchema,
};
