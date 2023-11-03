const InvariantError = require('../../exceptions/InvariantError');
const {
  UserPayloadSchema, UserSuccessCreateResponseSchema, UserGetResponseSchema, UserGetRequestSchema,
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
];

module.exports = routes;
