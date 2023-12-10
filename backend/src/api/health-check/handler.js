class HealthCheckHandler {
  constructor({ healthCheckService }) {
    this._healthCheckService = healthCheckService;
  }

  async checkHealth(_, h) {
    await this._healthCheckService.checkDatabase();

    await this._healthCheckService.checkCloudStorage();

    return h.response({
      success: true,
      message: 'Health check success',
    }).code(200);
  }
}

module.exports = HealthCheckHandler;
