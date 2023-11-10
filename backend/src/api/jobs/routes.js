const InvariantError = require('../../exceptions/InvariantError');
const { WithTokenRequestSchema } = require('../../validator/general/schema');
const { GetJobsFieldResponseSchema, GetJobsPositionResponseSchema } = require('../../validator/jobs/schema');

const routes = (handler) => [
  {
    method: 'GET',
    path: '/job/fields',
    options: {
      handler: (request, h) => handler.getJobFieldsHandler(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: GetJobsFieldResponseSchema },
    },
  },
  {
    method: 'GET',
    path: '/job/positions',
    options: {
      handler: (request, h) => handler.getJobPositionsHandler(request, h),
      auth: 'interviewku_jwt',
      validate: {
        headers: WithTokenRequestSchema,
        failAction: (request, h, error) => {
          throw new InvariantError(error.message);
        },
      },
      tags: ['api'],
      response: { schema: GetJobsPositionResponseSchema },
    },
  },
];

module.exports = routes;
