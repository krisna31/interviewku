/* eslint-disable no-unused-vars */
class AnswersHandler {
  constructor({ answersService }) {
    this._answersService = answersService;
  }

  async getAnswersById(request, h) {
    const id = request.params.questionId;

    const answers = await this._answersService.getAnswersById(id);

    return {
      success: true,
      message: 'Answers Ditemukan',
      data: {
        question: answers[0].question,
        answers: answers.map((answer) => ({
          answer: answer.answer,
        })),
      },
    };
  }
}

module.exports = AnswersHandler;
