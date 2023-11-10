const JobsHandler = require('./handler');
const routes = require('./routes');

module.exports = {
  name: 'jobs',
  version: '1.0.0',
  register: async (server, { jobsService }) => {
    const jobsHandler = new JobsHandler({ jobsService });
    server.route(routes(jobsHandler));
  },
};
