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
  .default('secretpassword1')
  .required();

const UserPayloadSchema = Joi.object({
  firstName: Joi.string().required().default('first name'),
  lastName: Joi.string(),
  email: Joi.string().email({ tlds: true }).default('interviewku@gmail.com').required(),
  password: passwordValidation,
}).label('Users Payload');

const UserSuccessCreateResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('User berhasil ditambahkan').required(),
  data: Joi.object({
    userId: Joi.string().default('user-xxx'),
  }),
}).label('User Response Success');

const UserGetResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('User Ditemukan').required(),
  data: Joi.object({
    id: Joi.string().default('user-xxx'),
    firstName: Joi.string().default('first name'),
    lastName: Joi.any().default(null),
    email: Joi.string().default('interviewku@gmail.com'),
    createdAt: Joi.date().default('2023-11-09T09:18:07.659Z'),
    updatedAt: Joi.any().default(null),
  }),
}).label('User Get By Id Success');

const PostUserIdentityPayloadSchema = Joi.object({
  jobPositionId: Joi.number().integer().default(1).required(),
  gender: Joi.string()
    .length(1)
    .valid('p', 'l', 'P', 'L')
    .default('L')
    .required(),
  dateBirth: Joi.date().default('2023-11-09T09:18:07.659Z').required(),
  currentCity: Joi.string().max(100).default('Riau').required(),
}).label('User Identity Payload');

const UserIdentityResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Identitas User berhasil ditambahkan').required(),
  data: Joi.object({
    userId: Joi.string().default('user-xxx'),
    jobPositionId: Joi.number().default(1).integer(),
    jobPositionName: Joi.string().default('Frontend'),
    gender: Joi.string().length(1).valid('p', 'l', 'P', 'L').default('L'),
    dateBirth: Joi.date().default('2023-11-09T09:18:07.659Z'),
    currentCity: Joi.string().max(100).default('Lampung'),
    createdAt: Joi.date().default('2023-11-09T09:18:07.659Z'),
    updatedAt: Joi.any().default(null),
  }),
}).label('GET | POST | PUT for User Identity Response Success');

const DeleteUserIdentityResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Identitas User berhasil dihapus').required(),
  data: Joi.object({
    userId: Joi.string().default('user-xxx'),
  }),
}).label('User Identity Response Success');

const PutChangeUserIdentityPayloadSchema = Joi.object({
  jobPositionId: Joi.number().integer().default(1),
  gender: Joi.string().length(1).valid('p', 'l', 'P', 'L').default('L'),
  dateBirth: Joi.date().default('2023-11-09T09:18:07.659Z'),
  currentCity: Joi.string().max(100).default('Palembang'),
}).label('PUT User Identity Payload');

module.exports = {
  UserPayloadSchema,
  UserSuccessCreateResponseSchema,
  UserGetResponseSchema,
  passwordValidation,
  PostUserIdentityPayloadSchema,
  UserIdentityResponseSchema,
  DeleteUserIdentityResponseSchema,
  PutChangeUserIdentityPayloadSchema,
};
