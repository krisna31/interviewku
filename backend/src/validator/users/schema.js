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
    id: Joi.string(),
    firstName: Joi.string(),
    lastName: Joi.any(),
    email: Joi.string(),
    createdAt: Joi.date(),
    updatedAt: Joi.any(),
  }),
}).label('User Get By Id Success');

const UserGetRequestSchema = Joi.object({
  id: Joi.string().required(),
}).label('Get User Params');

const PostUserIdentityPayloadSchema = Joi.object({
  jobPositionId: Joi.number().integer().required(),
  gender: Joi.string().length(1).valid('p', 'l', 'P', 'L').required(),
  dateBirth: Joi.date().required(),
  currentCity: Joi.string().max(100).required(),
}).label('User Identity Payload');

const PostUserIdentityResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
  data: Joi.object({
    userId: Joi.string(),
    jobPositionId: Joi.number().integer(),
    jobPositionName: Joi.string(),
    gender: Joi.string().length(1).valid('p', 'l', 'P', 'L'),
    dateBirth: Joi.date(),
    currentCity: Joi.string().max(100),
    createdAt: Joi.date(),
    updatedAt: Joi.any(),
  }),
}).label('User Identity Response Success');

const DeleteUserIdentityResponseSchema = Joi.object({
  status: Joi.string().valid('success').required(),
  message: Joi.string().required(),
  data: Joi.object({
    userId: Joi.string(),
  }),
}).label('User Identity Response Success');

const PutChangeUserIdentityPayloadSchema = Joi.object({
  jobPositionId: Joi.number().integer(),
  gender: Joi.string().length(1).valid('p', 'l', 'P', 'L'),
  dateBirth: Joi.date(),
  currentCity: Joi.string().max(100),
}).label('PUT User Identity Payload');

module.exports = {
  UserPayloadSchema,
  UserSuccessCreateResponseSchema,
  UserGetResponseSchema,
  UserGetRequestSchema,
  passwordValidation,
  PostUserIdentityPayloadSchema,
  PostUserIdentityResponseSchema,
  DeleteUserIdentityResponseSchema,
  PutChangeUserIdentityPayloadSchema,
};
