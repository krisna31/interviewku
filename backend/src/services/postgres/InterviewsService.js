/* eslint-disable class-methods-use-this */
/* eslint-disable no-plusplus */
const { Pool } = require('pg');
const { nanoid } = require('nanoid');
const InvariantError = require('../../exceptions/InvariantError');
const {
  getFeedback,
  getDateAfterXMinutes,
  changeToOneUntilFiveRange,
  // strukturScoreToFeedback,
} = require('../../utils');

class InterviewsService {
  constructor() {
    this._pool = new Pool();
  }

  async getQuestionsByInterviewId({ interviewId }) {
    const query = {
      text: `
        SELECT
          *
        FROM test_histories th
        INNER JOIN question_answer_histories qah ON th.id = qah.test_history_id
        WHERE th.id = $1
      `,
      values: [interviewId],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Sesi Interview Tidak Ditemukan');
    }

    return result.rows.map((ans) => ({
      // interviewId: ans.id,
      questionOrder: ans.question_order,
      jobFieldName: ans.job_field_name,
      // jobDescription: ans.job_description,
      question: ans.question,
    }));
  }

  async addInterview({
    userId,
    mode,
    totalQuestions,
    jobFieldName,
  }) {
    const id = `sesi-interview-${nanoid(16)}`;

    const query = {
      text: 'INSERT INTO test_histories (id, user_id, mode, total_questions, job_field_name) VALUES ($1, $2, $3, $4, $5) RETURNING id',
      values: [id, userId, mode, totalQuestions, jobFieldName],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Sesi Interview Gagal Dimulai, Silahkan Coba Lagi Nanti');
    }

    return result.rows[0].id;
  }

  async updateAnswerByInterviewId({
    interviewId,
    // jobFieldName,
    // jobPositionName,
    audioUrl,
    score,
    duration,
    retryAttempt,
    // eslint-disable-next-line no-unused-vars
    question,
    strukturScore,
    userAnswer,
    questionOrder,
  }) {
    const query = {
      text: `
          UPDATE question_answer_histories SET
            audio_url = $3,
            score = $4,
            duration = $5,
            retry_attempt = $6,
            struktur_score = $7,
            user_answer = $8,
            updated_at = $9
          WHERE test_history_id = $1 AND question_order = $2 RETURNING id
      `,
      values: [
        interviewId,
        questionOrder,
        audioUrl,
        score,
        duration,
        retryAttempt,
        strukturScore,
        userAnswer,
        new Date(),
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
    let text = 'INSERT INTO question_answer_histories (id, test_history_id, question, question_order, job_field_name) VALUES ';
    let queryIndex = 1;
    const values = [];

    questions.forEach((question, i) => {
      const questionAnswerHistoryId = `question-answer-${nanoid(16)}`;

      text += `
        ($${queryIndex++}, 
          $${queryIndex++}, 
          $${queryIndex++}, 
          $${queryIndex++}, 
          $${queryIndex++}
          ${i === questions.length - 1 ? ')' : '),'}`;

      values.push(
        questionAnswerHistoryId,
        interviewId,
        question.question,
        question.questionOrder,
        question.jobFieldName,
      );
    });

    return { text, values };
  }

  async validateIsInterviewCompleted({ interviewId }) {
    const query = {
      text: `
        SELECT (
          select total_questions from test_histories
          WHERE id = $1
        ) = (
          SELECT COUNT(*) FROM question_answer_histories
          WHERE test_history_id = $1
          AND user_answer IS NOT NULL
        ) AS completed
          FROM test_histories
          WHERE id = $1
      `,
      values: [interviewId],
    };

    const result = await this._pool.query(query);

    const { completed } = result.rows[0];

    if (!completed) {
      throw new InvariantError('Sesi Interview Belum Selesai, Silahkan Jawab Semua Pertanyaan');
    }

    return completed;
  }

  async editInterviewByInterviewId({ interviewId, completed }) {
    const query = {
      text: 'UPDATE test_histories SET completed = $2, updated_at = $3 WHERE id = $1 RETURNING id',
      values: [interviewId, completed, new Date()],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Sesi Interview Gagal Ditutup, Silahkan Coba Lagi Nanti');
    }

    return result.rows[0].id;
  }

  async getInterviewDataByInterviewId({ interviewId }) {
    const answers = await this.getAnswerDataByInterviewId(interviewId);

    const query = {
      text: `
          SELECT
          th.id,
          th.mode,
          th.total_questions,
          th.completed,
          th.job_field_name,
          AVG(qah.score) AS avg_score,
          AVG(qah.struktur_score) AS avg_struktur_score,
          AVG(qah.retry_attempt) AS avg_retry_attempt,
          SUM(qah.duration) AS total_duration,
          th.created_at,
          th.updated_at
        FROM test_histories th
        INNER JOIN question_answer_histories qah
          ON th.id = qah.test_history_id
        WHERE th.id = $1
        GROUP BY th.id
      `,
      values: [interviewId],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Sesi Interview Tidak Ditemukan');
    }

    const interviewSession = result.rows[0];

    return {
      interviewId: interviewSession.id,
      mode: interviewSession.mode,
      totalQuestions: interviewSession.total_questions,
      completed: interviewSession.completed,
      score: changeToOneUntilFiveRange(interviewSession.avg_score),
      // strukturScore: interviewSession.avg_struktur_score,
      // retryAttempt: interviewSession.avg_retry_attempt,
      totalDuration: interviewSession.total_duration,
      startedAt: interviewSession.created_at,
      jobFieldName: interviewSession.job_field_name,
      feedback: getFeedback(
        interviewSession.avg_score,
        interviewSession.avg_struktur_score,
        interviewSession.avg_retry_attempt,
      ),
      answers,
    };
  }

  async getAnswerDataByInterviewId(interviewId) {
    const query = {
      text: `
        SELECT
          *
        FROM question_answer_histories
        WHERE test_history_id = $1
      `,
      values: [interviewId],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Sesi Interview Tidak Ditemukan');
    }

    return result.rows.map((ans) => ({
      question: ans.question,
      questionOrder: ans.question_order,
      userAnswer: ans.user_answer,
      audioUrl: ans.audio_url,
      score: changeToOneUntilFiveRange(ans.score),
      duration: ans.duration,
      retryAttempt: ans.retry_attempt,
      // strukturFeedback: strukturScoreToFeedback(ans.struktur_score),
      jobFieldName: ans.job_field_name,
      feedback: getFeedback(ans.score, ans.struktur_score, ans.retry_attempt),
    }));
  }

  async generateInterviewToken({ interviewId }) {
    const token = nanoid(32);

    const query = {
      text: 'INSERT INTO interview_tokens (test_histories_id, token, expired_at) VALUES ($1, $2, $3) RETURNING token',
      values: [interviewId, token, getDateAfterXMinutes(new Date(), 60)],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Gagal membuat token interview');
    }

    return result.rows[0].token;
  }

  async validateInterviewToken({ interviewId, token }) {
    const query = {
      text: 'SELECT * FROM interview_tokens WHERE test_histories_id = $1 AND token = $2 AND expired_at > $3',
      values: [interviewId, token, new Date()],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Token tidak valid');
    }
  }

  async deleteInterviewToken({ interviewId }) {
    const query = {
      text: 'DELETE FROM interview_tokens WHERE test_histories_id = $1 OR expired_at < $2',
      values: [interviewId, new Date()],
    };

    await this._pool.query(query);
  }

  async validateIsInterviewClosed({ interviewId }) {
    const query = {
      text: 'SELECT * FROM interview_tokens WHERE test_histories_id = $1',
      values: [interviewId],
    };

    const result = await this._pool.query(query);

    if (result.rowCount > 0) {
      throw new InvariantError('Sesi Interview Masih Berlangsung');
    }
  }

  async getAllInterviewByUserId({ userId, limit, offset }) {
    const query = {
      text: `
        SELECT
          th.id,
          th.mode,
          th.total_questions,
          th.completed,
          th.job_field_name,
          AVG(qah.score) AS avg_score,
          AVG(qah.struktur_score) AS avg_struktur_score,
          AVG(qah.retry_attempt) AS avg_retry_attempt,
          SUM(qah.duration) AS total_duration,
          th.created_at,
          th.updated_at
        FROM test_histories th
        INNER JOIN question_answer_histories qah
          ON th.id = qah.test_history_id
        WHERE th.user_id = $1
        GROUP BY th.id
        ORDER BY th.created_at DESC
        LIMIT $2 OFFSET $3
      `,
      values: [userId, limit, offset],
    };

    const result = await this._pool.query(query);

    return result.rows.map((interview) => ({
      interviewId: interview.id,
      mode: interview.mode,
      totalQuestions: interview.total_questions,
      completed: interview.completed,
      score: changeToOneUntilFiveRange(interview.avg_score),
      // strukturScore: interview.avg_struktur_score,
      // retryAttempt: interview.avg_retry_attempt,
      totalDuration: interview.total_duration,
      jobFieldName: interview.job_field_name,
      feedback: getFeedback(
        interview.avg_score,
        interview.avg_struktur_score,
        interview.avg_retry_attempt,
      ),
      startedAt: interview.created_at,
    }));
  }

  async validateIsInterviewExist({ interviewId }) {
    const query = {
      text: 'SELECT id FROM test_histories WHERE id = $1',
      values: [interviewId],
    };

    const result = await this._pool.query(query);

    if (result.rowCount < 1) {
      throw new InvariantError('Sesi Interview Tidak Ditemukan');
    }
  }

  async getJobFieldNameByInterviewAndOrder({ interviewId, questionOrder }) {
    const query = {
      text: `
        SELECT job_field_name FROM question_answer_histories
        WHERE test_history_id = $1 AND question_order = $2
      `,
      values: [interviewId, questionOrder],
    };

    const result = await this._pool.query(query);

    return result.rows[0].job_field_name;
  }

  async getTotalDataByUserId({ userId }) {
    const query = {
      text: 'SELECT COUNT(*) FROM test_histories WHERE user_id = $1',
      values: [userId],
    };

    const result = await this._pool.query(query);

    return result.rows[0].count;
  }
}

module.exports = InterviewsService;
