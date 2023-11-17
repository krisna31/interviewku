const { randomInRange } = require('../../utils');

/* eslint-disable no-unused-vars */
class QuestionsHandler {
  constructor({ questionsService, interviewsService }) {
    this._questionsService = questionsService;
    this._interviewsService = interviewsService;
  }

  async getQuestions(request, h) {
    // get mode from query
    const { mode } = request.query;
    const { id: userId } = request.auth.credentials;

    // generate random total questions
    const totalQuestions = randomInRange(3, 5);

    // get questions by total questions
    const questions = await this._questionsService.getQuestionsByTotalQuestion(totalQuestions);

    // insert data to test_histories table with mode and total questions
    const interviewId = await this._interviewsService.addInterview({
      userId,
      mode,
      totalQuestions,
    });

    await this._interviewsService.addQuestionsToHistory({
      interviewId,
      questions,
    });

    return {
      success: true,
      message: `Sesi Interview ${interviewId} telah dimulai`,
      data: {
        interviewId,
        questions,
      },
    };
  }
}

module.exports = QuestionsHandler;
