const Joi = require('joi');
const { joiPasswordExtendCore } = require('joi-password');
const { passwordValidation } = require('../users/schema');

const joiPassword = Joi.extend(joiPasswordExtendCore);

const PostAuthenticationPayloadSchema = Joi.object({
  email: Joi.string().email({ tlds: true }).required(),
  password: Joi.string().required(),
});

const PostAuthenticationResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
  data: Joi.object({
    accessToken: Joi.string(),
    refreshToken: Joi.string(),
  }),
});

const PutAuthenticationPayloadSchema = Joi.object({
  refreshToken: Joi.string().required(),
});
const PutAuthenticationResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
  data: Joi.object({
    accessToken: Joi.string(),
  }),
});

const DeleteAuthenticationPayloadSchema = Joi.object({
  refreshToken: Joi.string().required(),
});

const DeleteAuthenticationResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
});

const PutChangePasswordPayloadSchema = Joi.object({
  oldPassword: Joi.string().required(),
  newPassword: passwordValidation,
});

const PutChangePasswordResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
});

module.exports = {
  PostAuthenticationPayloadSchema,
  PutAuthenticationPayloadSchema,
  DeleteAuthenticationPayloadSchema,
  PostAuthenticationResponseSchema,
  PutAuthenticationResponseSchema,
  DeleteAuthenticationResponseSchema,
  PutChangePasswordPayloadSchema,
  PutChangePasswordResponseSchema,
};
