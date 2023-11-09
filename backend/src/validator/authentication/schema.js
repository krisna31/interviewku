const Joi = require('joi');
const { passwordValidation } = require('../users/schema');

const PostAuthenticationPayloadSchema = Joi.object({
  email: Joi.string().email({ tlds: true }).required(),
  password: Joi.string().required(),
}).label('PostAuthenticationPayloadSchema');

const PostAuthenticationResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
  data: Joi.object({
    accessToken: Joi.string(),
    refreshToken: Joi.string(),
  }),
}).label('PostAuthenticationResponseSchema');

const PutAuthenticationPayloadSchema = Joi.object({
  refreshToken: Joi.string().required(),
}).label('PutAuthenticationPayloadSchema');

const PutAuthenticationResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
  data: Joi.object({
    accessToken: Joi.string(),
  }),
}).label('PutAuthenticationResponseSchema');

const DeleteAuthenticationPayloadSchema = Joi.object({
  refreshToken: Joi.string().required(),
}).label('DeleteAuthenticationPayloadSchema');

const DeleteAuthenticationResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
}).label('DeleteAuthenticationResponseSchema');

const PutChangePasswordPayloadSchema = Joi.object({
  oldPassword: Joi.string().required(),
  newPassword: passwordValidation,
}).label('PutChangePasswordPayloadSchema');

const PutChangePasswordResponseSchema = Joi.object({
  status: Joi.string().required(),
  message: Joi.string().required(),
}).label('PutChangePasswordResponseSchema');

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
