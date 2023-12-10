const InvariantError = require('../../exceptions/InvariantError');
const {
  GetPaginationQuerySchema,
} = require('../../validator/articles/schema');
const { PostChatPayloadSchema } = require('../../validator/chats/schema');
const { WithTokenRequestSchema } = require('../../validator/general/schema');

const routes = (handler) => [
  {
    method: 'POST',
    path: '/chats',
    options: {
      handler: (request, h) => handler.postChatHandler(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        payload: PostChatPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      // response: { schema: GetArticlesResponseSchema },
    },
  },
  {
    method: 'GET',
    path: '/chats',
    options: {
      handler: (request, h) => handler.getAllChatsHandler(request, h),
      auth: 'interviewku_jwt',
      validate: {
        query: GetPaginationQuerySchema,
        headers: WithTokenRequestSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      // response: { schema: GetArticlesResponseSchema },
    },
  },
  {
    method: 'GET',
    path: '/chats/{chatId}',
    options: {
      handler: (request, h) => handler.getChatById(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      // response: { schema: GetArticleResponseSchema },
    },
  },
];

module.exports = routes;
