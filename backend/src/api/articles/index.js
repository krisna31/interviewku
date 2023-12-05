const ArticlesHandler = require('./handler');
const routes = require('./routes');

module.exports = {
  name: 'articles',
  version: '1.0.0',
  register: async (server, { articlesService }) => {
    const articlesHandler = new ArticlesHandler({ articlesService });
    server.route(routes(articlesHandler));
  },
};
