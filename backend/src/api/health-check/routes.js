const InvariantError = require('../../exceptions/InvariantError');
const { StatusAndMessageResponseSchema } = require('../../validator/general/schema');

const routes = (handler) => [
  {
    method: 'GET',
    path: '/health-check',
    options: {
      handler: (request, h) => handler.checkHealth(request, h),
      validate: {
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: StatusAndMessageResponseSchema },
    },
  },
];

module.exports = routes;
