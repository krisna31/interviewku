const { randomInRange } = require('../../utils');

/* eslint-disable no-unused-vars */
class InterviewsHandler {
  constructor({
    questionsService,
    interviewsService,
    storageService,
    machineLearningService,
    audioService,
    jobsService,
  }) {
    this._questionsService = questionsService;
    this._interviewsService = interviewsService;
    this._storageService = storageService;
    this._machineLearningService = machineLearningService;
    this._audioService = audioService;
    this._jobsService = jobsService;
  }

  async getQuestions(request, h) {
    // get mode from query
    const {
      mode,
      jobFieldId,
    } = request.query;
    const { id: userId } = request.auth.credentials;

    const jobFieldName = await this._jobsService.getJobFieldNameByJobFieldId({
      jobFieldId,
    });

    // generate random total questions
    const totalQuestions = randomInRange(3, 5);

    // get questions by total questions
    const questions = await this._questionsService
      .getQuestionsByTotalQuestionAndJobFieldId({
        totalQuestions,
        jobFieldId,
      });

    // insert data to test_histories table with mode and total questions
    const interviewId = await this._interviewsService.addInterview({
      userId,
      mode,
      totalQuestions,
      jobFieldName,
    });

    const token = await this._interviewsService.generateInterviewToken({ interviewId });

    await this._interviewsService.addQuestionsToHistory({
      interviewId,
      questions,
    });

    return {
      success: true,
      message: `Sesi Interview ${interviewId} telah dimulai`,
      data: {
        interviewId,
        token,
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
      // jobPositionName,
      retryAttempt,
      question,
      questionOrder,
      token,
    } = request.payload;
    let testHistoryId;

    await this._interviewsService.validateIsInterviewExist({ interviewId });

    await this._interviewsService.validateInterviewToken({ interviewId, token });

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
        userAnswer,
      });

      testHistoryId = await this._interviewsService.updateAnswerByInterviewId({
        interviewId,
        // jobFieldName,
        // jobPositionName,
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
    const { completed, token } = request.payload;

    await this._interviewsService.validateIsInterviewExist({ interviewId });

    await this._interviewsService.validateInterviewToken({ interviewId, token });

    if (completed) {
      await this._interviewsService.validateIsInterviewCompleted({ interviewId });
    }

    await this._interviewsService.editInterviewByInterviewId({
      interviewId,
      completed,
    });

    await this._interviewsService.deleteInterviewToken({ interviewId });

    const interviewData = await this._interviewsService.getInterviewDataByInterviewId({
      interviewId,
    });

    return {
      success: true,
      message: `Sesi Interview ${interviewId} telah ditutup`,
      data: interviewData,
    };
  }

  async getInterview(request, h) {
    const { interviewId } = request.params;

    await this._interviewsService.validateIsInterviewExist({ interviewId });

    await this._interviewsService.validateIsInterviewClosed({ interviewId });

    const interviewData = await this._interviewsService.getInterviewDataByInterviewId({
      interviewId,
    });

    return {
      success: true,
      message: `Sesi Interview ${interviewId} ditemukan`,
      data: interviewData,
    };
  }

  async getAllInterview(request, h) {
    const { id: userId } = request.auth.credentials;

    const interviewData = await this._interviewsService.getAllInterviewByUserId({
      userId,
    });

    return {
      success: true,
      message: 'Sesi Interview ditemukan',
      data: interviewData,
    };
  }

  async getQuestionsByInterviewId(request, h) {
    const { interviewId } = request.params;

    await this._interviewsService.validateIsInterviewExist({ interviewId });

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
