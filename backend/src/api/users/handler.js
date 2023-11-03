/* eslint-disable no-unused-vars */
class UserHandler {
  constructor({ service, validator }) {
    this._service = service;
  }

  async postUserHandler(request, h) {
    const {
      firstName, lastName, email, password,
    } = request.payload;

    const userId = await this._service.addUser({
      firstName, lastName, email, password,
    });

    const response = h.response({
      status: 'success',
      message: 'User berhasil ditambahkan',
      data: {
        userId,
      },
    });
    response.code(201);
    return response;
  }

  async getUserByIdHandler(request, h) {
    const { id } = request.params;
    const user = await this._service.getUserById(id);
    return {
      status: 'success',
      message: 'User Ditemukan',
      data: {
        user,
      },
    };
  }
}

module.exports = UserHandler;
