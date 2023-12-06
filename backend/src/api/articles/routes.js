const InvariantError = require('../../exceptions/InvariantError');
const {
  GetArticlesResponseSchema,
  GetArticleResponseSchema,
  GetPaginationQuerySchema,
} = require('../../validator/articles/schema');
const { WithTokenRequestSchema } = require('../../validator/general/schema');

const routes = (handler) => [
  {
    method: 'GET',
    path: '/articles',
    options: {
      handler: (request, h) => handler.getAllArticlesHandler(request, h),
      auth: 'interviewku_jwt',
      validate: {
        query: GetPaginationQuerySchema,
        headers: WithTokenRequestSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: GetArticlesResponseSchema },
    },
  },
  {
    method: 'GET',
    path: '/articles/{articleId}',
    options: {
      handler: (request, h) => handler.getArticleById(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: GetArticleResponseSchema },
    },
  },
];

module.exports = routes;
