const HealthCheckHandler = require('./handler');
const routes = require('./routes');

module.exports = {
  name: 'heath-check',
  version: '1.0.0',
  register: async (server, { healthCheckService }) => {
    const healthCheckHandler = new HealthCheckHandler({ healthCheckService });
    server.route(routes(healthCheckHandler));
  },
};
