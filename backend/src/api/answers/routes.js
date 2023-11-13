const InvariantError = require('../../exceptions/InvariantError');
const { GetAnwersResponseSchema, GetAnwersRequestParamsSchema } = require('../../validator/answers/schema');
const { WithTokenRequestSchema } = require('../../validator/general/schema');

const routes = (handler) => [
  {
    method: 'GET',
    path: '/answers/{questionId}',
    options: {
      handler: (request, h) => handler.getAnswersById(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        params: GetAnwersRequestParamsSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: GetAnwersResponseSchema },
    },
  },
];

module.exports = routes;
