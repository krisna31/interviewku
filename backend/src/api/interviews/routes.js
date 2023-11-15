const InvariantError = require('../../exceptions/InvariantError');
const { WithTokenRequestSchema } = require('../../validator/general/schema');
const {
  PostAnswerParamsSchema,
  PostAnswerResponseSchema,
  PostAnswersPayloadByInterviewIdSchema,
} = require('../../validator/interview/schema');

const routes = (handler) => [
  {
    method: 'POST',
    path: '/interview/{interviewId}/answers',
    options: {
      handler: (request, h) => handler.postAnswerByInterviewId(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        params: PostAnswerParamsSchema,
        payload: PostAnswersPayloadByInterviewIdSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: PostAnswerResponseSchema },
      payload: {
        allow: 'multipart/form-data',
        multipart: true,
        output: 'stream',
        maxBytes: 2097152, // 2MB https://www.gigacalculator.com/converters/convert-mb-to-bytes.php
      },
    },
  },
];

module.exports = routes;
