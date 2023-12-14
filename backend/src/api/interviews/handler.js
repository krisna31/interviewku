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
    answersService,
  }) {
    this._questionsService = questionsService;
    this._interviewsService = interviewsService;
    this._storageService = storageService;
    this._machineLearningService = machineLearningService;
    this._audioService = audioService;
    this._jobsService = jobsService;
    this._answersService = answersService;
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

      const allAnswer = await this._answersService.getAllAnswers({ question });

      const jobFieldName = await this._interviewsService
        .getJobFieldNameByInterviewAndOrder({
          interviewId,
          questionOrder,
        });

      const strukturScore = await this._machineLearningService.getStrukturScore({
        userAnswer,
      });

      const score = await this._machineLearningService.getScore({
        userAnswer,
        field: jobFieldName,
        allAnswer,
        retryAttempt,
        strukturScore,
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

    // await this._interviewsService.validateIsInterviewClosed({ interviewId });

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
    const { page = 1, limit = 10 } = request.query;
    const startIndex = (page - 1) * limit;
    const endIndex = page * limit;

    const totalData = await this._interviewsService.getTotalDataByUserId({ userId });

    const interviewData = await this._interviewsService.getAllInterviewByUserId({
      userId,
      limit,
      offset: startIndex,
    });

    const baseUrl = process.env.APP_BASE_URL;

    return {
      success: true,
      message: 'Sesi Interview ditemukan',
      meta: {
        count: interviewData.length || 0,
        currentPage: +page,
        totalData: +totalData || 0,
        nextUrl: endIndex < totalData ? `${baseUrl}/interviews?page=${+page + 1}&limit=${limit}` : null,
        previousUrl: startIndex > 0 ? `${baseUrl}/interviews?page=${+page - 1}&limit=${limit}` : null,
        firstPageUrl: `${baseUrl}/interviews?page=1&limit=${limit}`,
        lastPageUrl: `${baseUrl}/interviews?page=${Math.ceil(totalData / limit)}&limit=${limit}`,
        limit: +limit,
      },
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
