const { Pool } = require('pg');
const InvariantError = require('../../exceptions/InvariantError');

class JobsService {
  constructor() {
    this._pool = new Pool();
  }

  async checkJobPositionId(jobPositionId) {
    const query = {
      text: 'SELECT id FROM job_positions WHERE id = $1',
      values: [jobPositionId],
    };

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new InvariantError('Job Position tidak valid');
    }
  }
}

module.exports = JobsService;
