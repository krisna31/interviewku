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

  async getJobFields() {
    const query = {
      text: 'SELECT * FROM job_fields',
    };

    const result = await this._pool.query(query);

    return result.rows.map((jobField) => ({
      id: jobField.id,
      name: jobField.name,
      description: jobField.description,
      // createdAt: jobField.created_at,
      // updatedAt: jobField.updated_at,
    }));
  }

  async getJobPositions() {
    const query = {
      text: 'SELECT * FROM job_positions',
    };

    const result = await this._pool.query(query);

    return result.rows.map((jobPosition) => ({
      id: jobPosition.id,
      name: jobPosition.name,
      description: jobPosition.description,
      // createdAt: jobPosition.created_at,
      // updatedAt: jobPosition.updated_at,
    }));
  }
}

module.exports = JobsService;
