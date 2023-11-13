const AnswersHandler = require('./handler');
const routes = require('./routes');

module.exports = {
  name: 'answers',
  version: '1.0.0',
  register: async (server, { answersService }) => {
    const answersHandler = new AnswersHandler({ answersService });
    server.route(routes(answersHandler));
  },
};
