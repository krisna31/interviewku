const { Pool } = require('pg');
const { nanoid } = require('nanoid');
const InvariantError = require('../../exceptions/InvariantError');

class InterviewsService {
  constructor() {
    this._pool = new Pool();
  }

  async addInterview({ userId, mode, totalQuestions }) {
    const id = `sesi-interview-${nanoid(16)}`;

    const query = {
      text: 'INSERT INTO test_histories (id, user_id, mode, total_questions) VALUES ($1, $2, $3, $4) RETURNING id',
      values: [id, userId, mode, totalQuestions],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Sesi Interview Gagal Dimulai, Silahkan Coba Lagi Nanti');
    }

    return result.rows[0].id;
  }
}

module.exports = InterviewsService;
