// Import library module
import Hapi from '@hapi/hapi';
import dotenv from 'dotenv';

// import user created module
import ClientError from './exceptions/ClientError';

dotenv.config();

(async () => {
  const server = Hapi.server({
    port: process.env.PORT,
    host: process.env.HOST,
    routes: {
      cors: true,
    },
  });

  server.route({
    method: 'GET',
    path: '/',
    handler: (request, h) => 'Hello World!',
  });

  // extension function before response check and handle error
  server.ext('onPreResponse', (request, h) => {
    const { response } = request;
    // console.log(response.message);
    if (response instanceof Error) {
      if (response instanceof ClientError) {
        const newResponse = h.response({
          status: 'fail',
          message: response.message,
        });
        newResponse.code(response.statusCode);
        return newResponse;
      }
      if (!response.isServer) {
        return h.continue;
      }
      const newResponse = h.response({
        status: 'error',
        message: 'terjadi kegagalan pada server kami',
      });
      newResponse.code(500);
      return newResponse;
    }
    return h.continue;
  });

  await server.start();
  console.log(`Server Berjalan di ${server.info.uri}`);
})();
