const Joi = require('joi');
const { joiPasswordExtendCore } = require('joi-password');

const joiPassword = Joi.extend(joiPasswordExtendCore);

const passwordValidation = joiPassword
  .string()
  // .minOfSpecialCharacters(2)
  // .minOfLowercase(2)
  // .minOfUppercase(2)
  .min(6)
  .max(30)
  .minOfNumeric(1)
  .noWhiteSpaces()
  .onlyLatinCharacters()
  .required();

const UserPayloadSchema = Joi.object({
  firstName: Joi.string().required(),
  lastName: Joi.string(),
  email: Joi.string().email({ tlds: true }).required(),
  password: passwordValidation,
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
  message: Joi.string().required(),
  data: Joi.object({
    user: Joi.object({
      id: Joi.string(),
      firstname: Joi.string(),
      lastname: Joi.any(),
      email: Joi.string(),
    }),
  }),
}).label('User Get By Id Success');

const UserGetRequestSchema = Joi.object({
  id: Joi.string().required(),
}).label('Get User Params');

module.exports = {
  UserPayloadSchema,
  UserSuccessCreateResponseSchema,
  UserGetResponseSchema,
  UserGetRequestSchema,
  passwordValidation,
};
