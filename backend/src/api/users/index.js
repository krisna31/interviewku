const UsersHandler = require('./handler');
const routes = require('./routes');

module.exports = {
  name: 'users',
  version: '1.0.0',
  register: async (server, { usersService, jobsService }) => {
    const usersHandler = new UsersHandler({ usersService, jobsService });
    server.route(routes(usersHandler));
  },
};
