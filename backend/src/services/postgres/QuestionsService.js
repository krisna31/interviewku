const { Pool } = require('pg');
const { nanoid } = require('nanoid');
const bcrypt = require('bcrypt');
const InvariantError = require('../../exceptions/InvariantError');
const NotFoundError = require('../../exceptions/NotFoundError');
const AuthenticationError = require('../../exceptions/AuthenticationError');

class QuestionsService {
  constructor() {
    this._pool = new Pool();
  }

  async getAllQuestions() {
    const query = {
      text: `
        SELECT * FROM questions AS q
        INNER JOIN job_fields AS jf ON q.job_field_id = jf.id;
      `,
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Data Masih Kosong')
    }

    return result.rows.map((question) => ({
      id: question.id,
      jobFieldName: question.name,
      jobDescription: question.description,
      question: question.question,
      // createdAt: question.created_at,
      // updatedAt: question.updated_at,
    }));
  }
}

module.exports = QuestionsService;
