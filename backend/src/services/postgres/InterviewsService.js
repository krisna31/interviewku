/* eslint-disable class-methods-use-this */
/* eslint-disable no-plusplus */
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

  async updateAnswerByInterviewId({
    interviewId,
    jobFieldName,
    jobPositionName,
    audioUrl,
    score,
    duration,
    retryAttempt,
    // eslint-disable-next-line no-unused-vars
    question,
    feedback,
    userAnswer,
    questionOrder,
  }) {
    const query = {
      text: `
          UPDATE question_answer_histories SET
            job_field_name = $3,
            job_position_name = $4,
            audio_url = $5,
            score = $6,
            duration = $7,
            retry_attempt = $8,
            feedback = $9,
            user_answer = $10
          WHERE test_history_id = $1 AND question_order = $2 RETURNING id
      `,
      values: [
        interviewId,
        questionOrder,
        jobFieldName,
        jobPositionName,
        audioUrl,
        score,
        duration,
        retryAttempt,
        feedback,
        userAnswer,
      ],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Jawaban gagal disimpan, Silahkan Coba Lagi Nanti');
    }

    return result.rows[0].id;
  }

  async addQuestionsToHistory({ interviewId, questions }) {
    const { text, values } = this.buildQueryInsertQuestionsToQaHistory(questions, interviewId);

    const query = {
      text,
      values,
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Sesi Interview Gagal Dimulai, Silahkan Coba Lagi Nanti');
    }
  }

  async validateInsertQuestionsToHistory({ interviewId, questionOrder, question }) {
    const query = {
      text: `
      SELECT * FROM question_answer_histories
        WHERE test_history_id = $1 AND question_order = $2 AND LOWER(question) = LOWER($3)`,
      values: [interviewId, questionOrder, question],
    };

    const result = await this._pool.query(query);

    if (!result.rows.length) {
      throw new InvariantError('Sesi Interview Tidak Valid, Silahkan cek kembali (!)');
    }

    return result.rows[0];
  }

  buildQueryInsertQuestionsToQaHistory(questions, interviewId) {
    let text = 'INSERT INTO question_answer_histories (id, test_history_id, question, question_order) VALUES ';
    let queryIndex = 1;
    const values = [];

    questions.forEach((question, i) => {
      const questionAnswerHistoryId = `question-answer-${nanoid(16)}`;

      text += `
        ($${queryIndex++}, 
          $${queryIndex++}, 
          $${queryIndex++}, 
          $${queryIndex++}
          ${i === questions.length - 1 ? ')' : '),'}`;

      values.push(questionAnswerHistoryId, interviewId, question.question, question.questionOrder);
    });

    return { text, values };
  }
}

module.exports = InterviewsService;
