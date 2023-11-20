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
    audioService,
  }) => {
    const interviewsHandler = new InterviewsHandler({
      questionsService,
      interviewsService,
      storageService,
      machineLearningService,
      audioService,
    });
    server.route(routes(interviewsHandler));
  },
};
