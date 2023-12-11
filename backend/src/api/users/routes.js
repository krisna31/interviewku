const InvariantError = require('../../exceptions/InvariantError');
const { WithTokenRequestSchema } = require('../../validator/general/schema');
const {
  UserPayloadSchema,
  UserSuccessCreateResponseSchema,
  UserGetResponseSchema,
  PostUserIdentityPayloadSchema,
  UserIdentityResponseSchema,
  DeleteUserIdentityResponseSchema,
  PutChangeUserIdentityPayloadSchema,
  PostIdentityResponseSchema,
} = require('../../validator/users/schema');

const routes = (handler) => [
  {
    method: 'POST',
    path: '/users',
    options: {
      handler: (request, h) => handler.postUserHandler(request, h),
      tags: ['api'],
      validate: {
        payload: UserPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: UserSuccessCreateResponseSchema },
    },
  },
  {
    method: 'GET',
    path: '/users',
    options: {
      handler: (request, h) => handler.getUserByIdHandler(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: UserGetResponseSchema },
    },
  },
  {
    method: 'POST',
    path: '/users/identity',
    options: {
      handler: (request, h) => handler.postUserIdentityHandler(request, h),
      auth: 'interviewku_jwt',
      tags: ['api'],
      validate: {
        headers: WithTokenRequestSchema,
        payload: PostUserIdentityPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: PostIdentityResponseSchema },
    },
  },
  {
    method: 'GET',
    path: '/users/identity',
    options: {
      handler: (request, h) => handler.getUserIdentityHandler(request, h),
      auth: 'interviewku_jwt',
      tags: ['api'],
      validate: {
        headers: WithTokenRequestSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: UserIdentityResponseSchema },
    },
  },
  {
    method: 'PUT',
    path: '/users/identity',
    options: {
      handler: (request, h) => handler.putUserIdentityHandler(request, h),
      auth: 'interviewku_jwt',
      tags: ['api'],
      validate: {
        headers: WithTokenRequestSchema,
        payload: PutChangeUserIdentityPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: UserIdentityResponseSchema },
    },
  },
  ...(process.env.APP_ENV === 'dev' ? [
    {
      method: 'DELETE',
      path: '/users/identity',
      options: {
        handler: (request, h) => handler.deleteUserIdentityHandler(request, h),
        auth: 'interviewku_jwt',
        tags: ['api'],
        validate: {
          headers: WithTokenRequestSchema,
          failAction: (request, h, error) => {
            throw new InvariantError(error.message);
          },
        },
        response: { schema: DeleteUserIdentityResponseSchema },
      },
    },
  ] : []),
];

module.exports = routes;
