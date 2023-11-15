const { Pool } = require('pg');
const InvariantError = require('../../exceptions/InvariantError');

class QuestionsService {
  constructor() {
    this._pool = new Pool();
  }

  async getQuestionsByTotalQuestion(totalQuestions) {
    const query = {
      text: `
        (
          SELECT q.question, jf.name AS job_field_name, jf.description AS job_field_description FROM questions AS q
          INNER JOIN job_fields AS jf ON q.job_field_id = jf.id
          WHERE job_field_id = 1
          ORDER BY RANDOM()
          LIMIT 1
        )
        UNION
        (
          SELECT q.question, jf.name AS job_field_name, jf.description AS job_field_description FROM questions AS q
          INNER JOIN job_fields AS jf ON q.job_field_id = jf.id
          WHERE job_field_id >= 6
          ORDER BY RANDOM()
          LIMIT $1
        )
        UNION
        (
          SELECT q.question, jf.name AS job_field_name, jf.description AS job_field_description FROM questions AS q
          INNER JOIN job_fields AS jf ON q.job_field_id = jf.id
          WHERE job_field_id = 5
          ORDER BY RANDOM()
          LIMIT 1
        );
      `,
      values: [totalQuestions - 2],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Data Masih Kosong');
    }

    return result.rows.map((question) => ({
      jobFieldName: question.job_field_name,
      jobDescription: question.job_field_description,
      question: question.question,
      // createdAt: question.created_at,
      // updatedAt: question.updated_at,
    }));
  }
}

module.exports = QuestionsService;
