const QuestionsHandler = require('./handler');
const routes = require('./routes');

module.exports = {
  name: 'questions',
  version: '1.0.0',
  register: async (server, { questionsService }) => {
    const questionsHandler = new QuestionsHandler({ questionsService });
    server.route(routes(questionsHandler));
  },
};
