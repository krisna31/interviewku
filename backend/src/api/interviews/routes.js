const InvariantError = require('../../exceptions/InvariantError');
const { WithTokenRequestSchema } = require('../../validator/general/schema');
const {
  PostAnswerParamsSchema,
  PostAnswerResponseSchema,
  PostAnswersPayloadByInterviewIdSchema,
  PutInterviewPayloadSchema,
  InterviewDataResponseSchema,
  GetQuestionsResponseSchema,
  GetQuestionsQuerySchema,
} = require('../../validator/interview/schema');

const routes = (handler) => [
  {
    method: 'GET',
    path: '/interview/questions',
    options: {
      handler: (request, h) => handler.getQuestions(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        query: GetQuestionsQuerySchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: GetQuestionsResponseSchema },
    },
  },
  {
    method: 'GET',
    path: '/interview/{interviewId}/questions',
    options: {
      handler: (request, h) => handler.getQuestionsByInterviewId(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        params: PostAnswerParamsSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: GetQuestionsResponseSchema },
    },
  },
  {
    method: 'GET',
    path: '/interview/{interviewId}',
    options: {
      handler: (request, h) => handler.getInterview(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        params: PostAnswerParamsSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: InterviewDataResponseSchema },
    },
  },
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
        output: 'file',
        maxBytes: 1572864, // 1.5MB https://www.gigacalculator.com/converters/convert-mb-to-bytes.php
      },
    },
  },
  {
    method: 'PUT',
    path: '/interview/{interviewId}',
    options: {
      handler: (request, h) => handler.closeInterviewSession(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        params: PostAnswerParamsSchema,
        payload: PutInterviewPayloadSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: InterviewDataResponseSchema },
    },
  },
];

module.exports = routes;
