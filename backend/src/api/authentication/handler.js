const { utcToLocalTimeZone } = require('../../utils');

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
    this._READY_FOR_CHANGE_PASSWORD = 'READY';
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
      <div style="font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2">
        <div style="margin:50px auto;width:70%;padding:20px 0">
          <div style="border-bottom:1px solid #eee">
            <a href="" style="font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600">InterviewKu</a>
          </div>
          <p style="font-size:1.1em">Hi,</p>
          <p>Use the following OTP to complete your reset password.</p>
          <h2 style="background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;">${otp}</h2>
          <p style="font-size:0.9em;">Regards,<br />InterviewKu</p>
          <hr style="border:none;border-top:1px solid #eee" />
          <div style="float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300">
            <p>InterviewKu Company</p>
            <p>Palembang, Indonesia</p>
      <!--       <p>Indonesia</p> -->
          </div>
        </div>
      </div>
    `;

    const expiredAt = await this._usersService.addOtpToUser(email, otp);

    await this._mailService.sendEmail(email, content);

    return {
      success: true,
      message: `Kode OTP telah dikirimkan ke email ${email} dan berlaku hingga ${utcToLocalTimeZone(expiredAt)}`,
    };
  }

  async verifyOtp(request, h) {
    const { email, otp } = request.payload;

    await this._usersService.verifyOtp(email, otp);

    await this._usersService.updateOtpToUser(email, this._READY_FOR_CHANGE_PASSWORD);

    return {
      success: true,
      message: 'Otp Valid Silahkan Ubah Password',
    };
  }

  async changePassword(request, h) {
    const { email, newPassword } = request.payload;

    await this._usersService.verifyOtp(email, this._READY_FOR_CHANGE_PASSWORD);

    await this._usersService.editUserPasswordByEmail(email, newPassword);

    await this._usersService.deleteOtpFromUser(email);

    return {
      success: true,
      message: 'Password berhasil diperbarui',
    };
  }
}

module.exports = AuthenticationsHandler;
