const InvariantError = require('../../exceptions/InvariantError');
const {
  PostAuthenticationPayloadSchema,
  PostAuthenticationResponseSchema,
  PutAuthenticationPayloadSchema,
  PutAuthenticationResponseSchema,
  DeleteAuthenticationPayloadSchema,
  DeleteAuthenticationResponseSchema,
} = require('../../validator/authentication/schema');

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
];

module.exports = routes;
