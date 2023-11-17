const InterviewsHandler = require('./handler');
const routes = require('./routes');

module.exports = {
  name: 'interviews',
  version: '1.0.0',
  register: async (server, {
    questionsService,
    interviewsService,
    storageService,
    machineLearningService,
  }) => {
    const interviewsHandler = new InterviewsHandler({
      questionsService,
      interviewsService,
      storageService,
      machineLearningService,
    });
    server.route(routes(interviewsHandler));
  },
};