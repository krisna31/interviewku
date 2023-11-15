const QuestionsHandler = require('./handler');
const routes = require('./routes');

module.exports = {
  name: 'questions',
  version: '1.0.0',
  register: async (server, { questionsService, interviewsService }) => {
    const questionsHandler = new QuestionsHandler({ questionsService, interviewsService });
    server.route(routes(questionsHandler));
  },
};
