const { Pool } = require('pg');
const InvariantError = require('../../exceptions/InvariantError');

class AnswersService {
  constructor() {
    this._pool = new Pool();
  }

  async getAnswersById(id) {
    const query = {
      text: `
        SELECT * FROM answers AS a
        INNER JOIN questions AS q ON a.question_id = q.id
        WHERE a.question_id = $1;
      `,
      values: [id],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Data Masih Kosong');
    }

    return result.rows;
  }

  async getAllAnswers({ question }) {
    const query = {
      text: `
      SELECT answer
      FROM answers 
        where question_id = (
          select id from questions
          where question = $1
        )
      `,
      values: [question],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Data Masih Kosong');
    }

    return result.rows;
  }
}

module.exports = AnswersService;
