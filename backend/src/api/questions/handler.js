/* eslint-disable no-unused-vars */
class QuestionsHandler {
  constructor({ questionsService }) {
    this._questionsService = questionsService;
  }

  async getQuestions(request, h) {
    const questions = await this._questionsService.getAllQuestions();

    return {
      success: true,
      message: 'Questions Ditemukan',
      data: {
        questions: questions,
      },
    };
  }
}

module.exports = QuestionsHandler;
