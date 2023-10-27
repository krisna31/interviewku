const Joi = require('joi');

const UserPayloadSchema = Joi.object({
  firstName: Joi.string().required(),
  lastName: Joi.string(),
  email: Joi.string().email({ tlds: true }).required(),
  password: Joi.string().required().min(6).max(30),
}).label('Users Payload');

const UserSuccessCreateResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
  data: Joi.object({
    userId: Joi.string(),
  }),
}).label('User Response Success');

const UserGetResponseSchema = Joi.object({
  status: Joi.string().required(),
  data: Joi.object({
    user: Joi.object({
      id: Joi.string(),
      firstname: Joi.string(),
      lastname: Joi.any(),
      email: Joi.string(),
    }),
  }),
}).label('User Get By Id Success');

module.exports = { UserPayloadSchema, UserSuccessCreateResponseSchema, UserGetResponseSchema };
