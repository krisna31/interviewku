const { randomInRange } = require('../../utils');

/* eslint-disable no-unused-vars */
class InterviewsHandler {
  constructor({
    questionsService,
    interviewsService,
    storageService,
    machineLearningService,
    audioService,
  }) {
    this._questionsService = questionsService;
    this._interviewsService = interviewsService;
    this._storageService = storageService;
    this._machineLearningService = machineLearningService;
    this._audioService = audioService;
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
    const { id: userId } = request.auth.credentials;
    const { interviewId } = request.params;
    const {
      audio,
      // jobFieldName,
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
      const duration = await this._audioService.validateAudio(audio);

      const audioUrl = await this._storageService.saveToCloudStorage(audio, userId);

      const userAnswer = await this._audioService.convertAudioToText(audio);

      const score = await this._machineLearningService.getScore({
        userAnswer,
      });

      const strukturScore = await this._machineLearningService.getStrukturScore({
        score,
        question,
      });

      testHistoryId = await this._interviewsService.updateAnswerByInterviewId({
        interviewId,
        // jobFieldName,
        jobPositionName,
        audioUrl,
        score,
        duration,
        retryAttempt,
        question,
        strukturScore,
        userAnswer,
        questionOrder,
      });
    }

    return {
      success: true,
      message: `Jawaban berhasil disimpan${userAlreadyAnswered ? ' (D)' : ''}`,
      data: {
        testHistoryId: userAlreadyAnswered ? testHistoryIdInDb : testHistoryId,
      },
    };
  }

  async closeInterviewSession(request, h) {
    const { interviewId } = request.params;
    const { completed } = request.payload;

    await this._interviewsService.validateIsInterviewCompleted({ interviewId });

    await this._interviewsService.editInterviewByInterviewId({
      interviewId,
      completed,
    });

    const interviewData = await this._interviewsService.getInterviewDataByInterviewId(
      {
        interviewId,
      },
    );

    return {
      success: true,
      message: `Sesi Interview ${interviewId} telah ditutup`,
      data: interviewData,
    };
  }

  async getInterview(request, h) {
    const { interviewId } = request.params;

    await this._interviewsService.validateIsInterviewCompleted({ interviewId });

    const interviewData = await this._interviewsService.getInterviewDataByInterviewId(
      {
        interviewId,
      },
    );

    return {
      success: true,
      message: `Sesi Interview ${interviewId} ditemukan`,
      data: interviewData,
    };
  }

  async getQuestionsByInterviewId(request, h) {
    const { interviewId } = request.params;

    const questions = await this._interviewsService.getQuestionsByInterviewId({
      interviewId,
    });

    return {
      success: true,
      message: `Sesi Interview ${interviewId} ditemukan`,
      data: {
        interviewId,
        questions,
      },
    };
  }
}

module.exports = InterviewsHandler;
