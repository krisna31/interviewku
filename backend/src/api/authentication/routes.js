const InvariantError = require('../../exceptions/InvariantError');
const {
  PostAuthenticationPayloadSchema,
  PostAuthenticationResponseSchema,
  PutAuthenticationPayloadSchema,
  PutAuthenticationResponseSchema,
  DeleteAuthenticationPayloadSchema,
  DeleteAuthenticationResponseSchema,
  PutChangePasswordResponseSchema,
  PutChangePasswordPayloadSchema,
  ResetPasswordPayloadSchema,
  ResetPasswordResponseSchema,
  VerifyOtpPayloadSchema,
  VerifyOtpResponseSchema,
  ChangePasswordPayloadSchema,
  ChangePasswordResponseSchema,
} = require('../../validator/authentication/schema');
const { WithTokenRequestSchema } = require('../../validator/general/schema');

const routes = (handler) => [
  {
    method: 'POST',
    path: '/authentications',
    options: {
      handler: (request, h) => handler.postAuthenticationHandler(request, h),
      tags: ['api'],
      validate: {
        payload: PostAuthenticationPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: PostAuthenticationResponseSchema },
    },
  },
  {
    method: 'PUT',
    path: '/authentications',
    options: {
      handler: (request, h) => handler.putAuthenticationHandler(request, h),
      tags: ['api'],
      validate: {
        payload: PutAuthenticationPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: PutAuthenticationResponseSchema },
    },
  },
  {
    method: 'DELETE',
    path: '/authentications',
    options: {
      handler: (request, h) => handler.deleteAuthenticationHandler(request, h),
      tags: ['api'],
      validate: {
        payload: DeleteAuthenticationPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: DeleteAuthenticationResponseSchema },
    },
  },
  {
    method: 'PUT',
    path: '/change-password',
    options: {
      handler: (request, h) => handler.changePasswordHandler(request, h),
      auth: 'interviewku_jwt',
      tags: ['api'],
      validate: {
        headers: WithTokenRequestSchema,
        payload: PutChangePasswordPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: PutChangePasswordResponseSchema },
    },
  },
  {
    method: 'POST',
    path: '/reset-password',
    options: {
      handler: (request, h) => handler.sendOtpToEmail(request, h),
      tags: ['api'],
      validate: {
        payload: ResetPasswordPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: ResetPasswordResponseSchema },
    },
  },
  {
    method: 'POST',
    path: '/reset-password/verify',
    options: {
      handler: (request, h) => handler.verifyOtp(request, h),
      tags: ['api'],
      validate: {
        payload: VerifyOtpPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: VerifyOtpResponseSchema },
    },
  },
  {
    method: 'PUT',
    path: '/reset-password',
    options: {
      handler: (request, h) => handler.changePassword(request, h),
      tags: ['api'],
      validate: {
        payload: ChangePasswordPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: ChangePasswordResponseSchema },
    },
  },
];

module.exports = routes;
