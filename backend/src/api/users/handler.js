/* eslint-disable no-unused-vars */
class UserHandler {
  constructor({ usersService, jobsService }) {
    this._usersService = usersService;
    this._jobsService = jobsService;
  }

  async postUserHandler(request, h) {
    const {
      firstName, lastName, email, password,
    } = request.payload;

    const userId = await this._usersService.addUser({
      firstName, lastName, email, password,
    });

    const response = h.response({
      success: true,
      message: 'User berhasil ditambahkan',
      data: {
        userId,
      },
    });
    response.code(201);
    return response;
  }

  async getUserByIdHandler(request, h) {
    // const { id } = request.params;
    const { id } = request.auth.credentials;
    const user = await this._usersService.getUserById(id);
    return {
      success: true,
      message: 'User Ditemukan',
      data: {
        id: user.id,
        firstName: user.first_name,
        lastName: user.last_name,
        email: user.email,
        createdAt: user.created_at,
        updatedAt: user.updated_at,
      },
    };
  }

  async postUserIdentityHandler(request, h) {
    const { id } = request.auth.credentials;
    const {
      jobPositionId,
      gender,
      dateBirth,
      currentCity,
    } = request.payload;

    await this._jobsService.checkJobPositionId(jobPositionId);

    const userIdentity = await this._usersService.addUserIdentity({
      userId: id,
      jobPositionId,
      gender: gender.toUpperCase(),
      dateBirth,
      currentCity,
    });

    return {
      success: true,
      message: 'Identitas User berhasil ditambahkan',
      data: {
        userId: userIdentity.user_id,
        jobPositionId: userIdentity.job_position_id,
        gender: userIdentity.gender,
        dateBirth: userIdentity.date_birth,
        currentCity: userIdentity.current_city,
        createdAt: userIdentity.created_at,
        updatedAt: userIdentity.updated_at,
      },
    };
  }

  async putUserIdentityHandler(request, h) {
    const { id } = request.auth.credentials;

    const {
      firstName,
      lastName,
      jobPositionId,
      gender,
      dateBirth,
      currentCity,
    } = request.payload;

    if (jobPositionId) await this._jobsService.checkJobPositionId(jobPositionId);

    const userIdentity = await this._usersService.editUserIdentity({
      firstName,
      lastName,
      userId: id,
      jobPositionId,
      gender: gender !== undefined ? gender.toUpperCase() : undefined,
      dateBirth,
      currentCity,
    });

    return {
      success: true,
      message: 'Identitas User Berhasil Diubah',
      data: {
        userId: userIdentity.user_id,
        firstName: userIdentity.first_name,
        lastName: userIdentity.last_name,
        email: userIdentity.email,
        jobPositionId: userIdentity.job_position_id,
        gender: userIdentity.gender,
        dateBirth: userIdentity.date_birth,
        currentCity: userIdentity.current_city,
        createdAt: userIdentity.created_at,
        updatedAt: userIdentity.updated_at,
      },
    };
  }

  async deleteUserIdentityHandler(request, h) {
    const { id } = request.auth.credentials;
    await this._usersService.deleteUserIdentity(id);
    return {
      success: true,
      message: 'Identitas User berhasil dihapus',
    };
  }

  async getUserIdentityHandler(request, h) {
    const { id } = request.auth.credentials;

    const userIdentity = await this._usersService.getUserIdentity(id);

    return {
      success: true,
      message: 'Identitas User Ditemukan',
      data: {
        userId: userIdentity.user_id,
        firstName: userIdentity.first_name,
        lastName: userIdentity.last_name,
        email: userIdentity.email,
        jobFieldId: userIdentity.job_field_id,
        jobFieldName: userIdentity.job_field_name,
        jobPositionId: userIdentity.job_position_id,
        jobPositionName: userIdentity.job_position_name,
        gender: userIdentity.gender,
        dateBirth: userIdentity.date_birth,
        currentCity: userIdentity.current_city,
        createdAt: userIdentity.created_at,
        updatedAt: userIdentity.updated_at,
      },
    };
  }
}

module.exports = UserHandler;
