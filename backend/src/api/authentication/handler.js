/* eslint-disable no-unused-vars */
class AuthenticationsHandler {
  constructor({
    authenticationsService,
    usersService,
    tokenManager,
    mailService,
  }) {
    this._authenticationsService = authenticationsService;
    this._usersService = usersService;
    this._tokenManager = tokenManager;
    this._mailService = mailService;
  }

  async postAuthenticationHandler(request, h) {
    const { email, password } = request.payload;
    const id = await this._usersService.verifyUserCredential(email, password);

    const accessToken = this._tokenManager.generateAccessToken({ id });
    const refreshToken = this._tokenManager.generateRefreshToken({ id });

    await this._authenticationsService.addRefreshToken(refreshToken);

    const response = h.response({
      success: true,
      message: 'Authentication berhasil ditambahkan',
      data: {
        accessToken,
        refreshToken,
      },
    });
    response.code(201);
    return response;
  }

  async putAuthenticationHandler(request, h) {
    const { refreshToken } = request.payload;
    await this._authenticationsService.verifyRefreshToken(refreshToken);
    const { id } = this._tokenManager.verifyRefreshToken(refreshToken);

    const accessToken = this._tokenManager.generateAccessToken({ id });
    return {
      success: true,
      message: 'Access Token berhasil diperbarui',
      data: {
        accessToken,
      },
    };
  }

  async deleteAuthenticationHandler(request, h) {
    const { refreshToken } = request.payload;
    await this._authenticationsService.verifyRefreshToken(refreshToken);
    await this._authenticationsService.deleteRefreshToken(refreshToken);

    return {
      success: true,
      message: 'Refresh token berhasil dihapus',
    };
  }

  async changePasswordHandler(request, h) {
    const { id } = request.auth.credentials;
    const { oldPassword, newPassword } = request.payload;

    await this._usersService.verifyUserCredentialById(id, oldPassword);
    await this._usersService.editUserPassword(id, newPassword);

    return {
      success: true,
      message: 'Password berhasil diperbarui',
    };
  }

  async sendOtpToEmail(request, h) {
    const { email } = request.payload;
    await this._usersService.verifyAvaliableEmail(email);
    await this._usersService.deleteOtpFromUser(email);

    const otp = this._authenticationsService.generateSixDigitCode();

    const content = `
      <b> OTP: ${otp} </b>
      <h1> JANGAN MEMBERITAHUKAN OTP INI KEPADA SIAPAPUN </h1>
    `;

    await this._usersService.addOtpToUser(email, otp);

    await this._mailService.sendEmail(email, content);

    return {
      success: true,
      message: 'Email Sudah Dikirimkan',
    };
  }

  async verifyOtp(request, h) {
    const { email, otp } = request.payload;

    await this._usersService.verifyOtp(email, otp);

    await this._usersService.updateOtpToUser(email, '888888');

    return {
      success: true,
      message: 'Otp Valid Silahkan Ubah Password',
    };
  }

  async changePassword(request, h) {
    const { email, newPassword } = request.payload;

    await this._usersService.verifyOtp(email, '888888');

    await this._usersService.editUserPasswordByEmail(email, newPassword);

    await this._usersService.deleteOtpFromUser(email);

    return {
      success: true,
      message: 'Password berhasil diperbarui',
    };
  }
}

module.exports = AuthenticationsHandler;
