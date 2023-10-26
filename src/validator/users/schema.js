const Joi = require('joi');

const UserPayloadSchema = Joi.object({
  firstName: Joi.string().required(),
  lastName: Joi.string(),
  email: Joi.string().email({ tlds: true }).required(),
  password: Joi.string().required().min(6).max(30),
});

module.exports = { UserPayloadSchema };
