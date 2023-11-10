/* eslint-disable no-unused-vars */
class JobsHandler {
  constructor({ jobsService }) {
    this._jobsService = jobsService;
  }

  async getJobFieldsHandler(request, h) {
    const { id } = request.auth.credentials;

    const jobFields = await this._jobsService.getJobFields(id);

    return {
      status: 'success',
      message: 'Jobs Field Ditemukan',
      data: {
        jobFields,
      },
    };
  }

  async getJobPositionsHandler(request, h) {
    const jobPositions = await this._jobsService.getJobPositions();

    return {
      status: 'success',
      message: 'Jobs Position Ditemukan',
      data: {
        jobPositions,
      },
    };
  }
}

module.exports = JobsHandler;
