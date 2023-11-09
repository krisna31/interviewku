const InvariantError = require('../../exceptions/InvariantError');
const {
  UserPayloadSchema,
  UserSuccessCreateResponseSchema,
  UserGetResponseSchema,
  UserGetRequestSchema,
  PostUserIdentityPayloadSchema,
  PostUserIdentityResponseSchema,
  DeleteUserIdentityResponseSchema,
  PutChangeUserIdentityPayloadSchema,
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
    path: '/users/{id}',
    options: {
      handler: (request, h) => handler.getUserByIdHandler(request, h),
      auth: 'interviewku_jwt',
      validate: {
        params: UserGetRequestSchema,
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
        payload: PostUserIdentityPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: PostUserIdentityResponseSchema },
    },
  },
  {
    method: 'GET',
    path: '/users/identity',
    options: {
      handler: (request, h) => handler.getUserIdentityHandler(request, h),
      auth: 'interviewku_jwt',
      tags: ['api'],
      response: { schema: PostUserIdentityResponseSchema },
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
        payload: PutChangeUserIdentityPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      response: { schema: PostUserIdentityResponseSchema },
    },
  },
  {
    method: 'DELETE',
    path: '/users/identity',
    options: {
      handler: (request, h) => handler.deleteUserIdentityHandler(request, h),
      auth: 'interviewku_jwt',
      tags: ['api'],
      response: { schema: DeleteUserIdentityResponseSchema },
    },
  },
];

module.exports = routes;
