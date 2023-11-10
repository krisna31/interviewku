const Joi = require('joi');
const { passwordValidation } = require('../users/schema');

const PostAuthenticationPayloadSchema = Joi.object({
  email: Joi.string().email({ tlds: true }).default('interviewku@gmail.com').required(),
  password: Joi.string().required(),
}).label('PostAuthenticationPayloadSchema');

const PostAuthenticationResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Authentication berhasil ditambahkan').required(),
  data: Joi.object({
    accessToken: Joi.string().default('xxx'),
    refreshToken: Joi.string().default('xxx'),
  }),
}).label('PostAuthenticationResponseSchema');

const PutAuthenticationPayloadSchema = Joi.object({
  refreshToken: Joi.string().default('xxx').required(),
}).label('PutAuthenticationPayloadSchema');

const PutAuthenticationResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Access Token berhasil diperbarui').required(),
  data: Joi.object({
    accessToken: Joi.string(),
  }),
}).label('PutAuthenticationResponseSchema');

const DeleteAuthenticationPayloadSchema = Joi.object({
  refreshToken: Joi.string().default('xxx').required(),
}).label('DeleteAuthenticationPayloadSchema');

const DeleteAuthenticationResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Refresh token berhasil dihapus').required(),
}).label('DeleteAuthenticationResponseSchema');

const PutChangePasswordPayloadSchema = Joi.object({
  oldPassword: Joi.string().default('password123').required(),
  newPassword: passwordValidation,
}).label('PutChangePasswordPayloadSchema');

const PutChangePasswordResponseSchema = Joi.object({
  success: Joi.boolean().default(true).required(),
  message: Joi.string().default('Password berhasil diperbarui').required(),
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
