const { randomInRange } = require('../../utils');

/* eslint-disable no-unused-vars */
class InterviewsHandler {
  constructor({
    questionsService,
    interviewsService,
    storageService,
    machineLearningService,
  }) {
    this._questionsService = questionsService;
    this._interviewsService = interviewsService;
    this._storageService = storageService;
    this._machineLearningService = machineLearningService;
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

  async postAnswerByInterviewId(request, h) {
    // const { id: userId } = request.auth.credentials;
    const { interviewId } = request.params;
    const {
      audio,
      jobFieldName,
      jobPositionName,
      retryAttempt,
      question,
      questionOrder,
    } = request.payload;
    let testHistoryId;

    const {
      user_answer: userAnswerInDb,
      test_history_id: testHistoryIdInDb,
    } = await this._interviewsService.validateInsertQuestionsToHistory({
      interviewId,
      questionOrder,
      question,
    });

    const userAlreadyAnswered = userAnswerInDb !== null;

    if (!userAlreadyAnswered) {
      const {
        audioUrl,
        userAnswer,
        duration,
      } = await this._storageService.saveToCloudStorage(audio);

      const score = await this._machineLearningService.getScore({
        userAnswer,
      });

      const feedback = await this._machineLearningService.getFeedback({
        score,
        question,
      });

      testHistoryId = await this._interviewsService.updateAnswerByInterviewId({
        interviewId,
        jobFieldName,
        jobPositionName,
        audioUrl,
        score,
        duration,
        retryAttempt,
        question,
        feedback,
        userAnswer,
        questionOrder,
      });
    }

    return {
      success: true,
      message: `Jawaban berhasil disimpan ${userAlreadyAnswered ? '(D)' : ''}`,
      data: {
        testHistoryId: userAlreadyAnswered ? testHistoryIdInDb : testHistoryId,
      },
    };
  }
}

module.exports = InterviewsHandler;
