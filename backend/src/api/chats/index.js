const ChatsHandler = require('./handler');
const routes = require('./routes');

module.exports = {
  name: 'chats',
  version: '1.0.0',
  register: async (server, { chatsService, machineLearningService }) => {
    const chatsHandler = new ChatsHandler({ chatsService, machineLearningService });
    server.route(routes(chatsHandler));
  },
};
