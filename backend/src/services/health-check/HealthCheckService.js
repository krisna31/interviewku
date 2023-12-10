const { Storage } = require('@google-cloud/storage');
const { Pool } = require('pg');
const InvariantError = require('../../exceptions/InvariantError');

class HealthCheckService {
  constructor() {
    this._pool = new Pool();
    const storage = process.env.GOOGLE_SERVICE_ACCOUNT_KEY !== 'false' ? new Storage({
      keyFilename: process.env.GOOGLE_SERVICE_ACCOUNT_KEY,
    }) : new Storage();
    const bucket = storage.bucket(process.env.AUDIO_BUCKET_NAME);

    this._bucket = bucket;
  }

  async checkDatabase() {
    const query = {
      text: 'SELECT NOW()',
    };

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new InvariantError('Ada masalah dengan database');
    }

    return result.rows[0].now;
  }

  async checkCloudStorage() {
    // get one random data from cloud storage
    const [files] = await this._bucket.getFiles();

    return files;
  }
}

module.exports = HealthCheckService;
