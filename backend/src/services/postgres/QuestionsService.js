const { Pool } = require('pg');

class QuestionsService {
  constructor() {
    this._pool = new Pool();
  }

  async getQuestionsByTotalQuestionAndJobFieldId({
    totalQuestions,
    jobFieldId,
  }) {
    const orderQuestion = 0;

    const openingQuestion = await this.getRandomQuestionByJobId({
      jobFieldId: 1,
      totalQuestions: 1,
      orderQuestion,
    });

    const mainQuestions = await this.getRandomQuestionByJobId({
      jobFieldId,
      totalQuestions: totalQuestions - 2,
      orderQuestion: orderQuestion + 1,
    });

    const closingQuestion = await this.getRandomQuestionByJobId({
      jobFieldId: 5,
      totalQuestions: 1,
      orderQuestion: orderQuestion + (1 + (totalQuestions - 2)),
    });

    return openingQuestion.concat(mainQuestions, closingQuestion);
  }

  async getRandomQuestionByJobId({ jobFieldId, totalQuestions, orderQuestion }) {
    const query = {
      text: `
        SELECT q.question, jf.name AS job_field_name, jf.description AS job_field_description FROM questions AS q
        INNER JOIN job_fields AS jf ON q.job_field_id = jf.id
        WHERE job_field_id = $1
        ORDER BY RANDOM()
        LIMIT $2
      `,
      values: [jobFieldId, totalQuestions],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new Error('Data Masih Kosong');
    }

    let questionOrder = orderQuestion;

    return result.rows.map((question) => {
      questionOrder += 1;

      return {
        questionOrder,
        jobFieldName: question.job_field_name,
        // jobDescription: question.job_field_description,
        question: question.question,
        // createdAt: question.created_at,
        // updatedAt: question.updated_at,
      };
    });
  }
}

module.exports = QuestionsService;
