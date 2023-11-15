/* eslint-disable no-unused-vars */
class InterviewsHandler {
  constructor({ interviewsService, storageService, machineLearningService }) {
    this._interviewsService = interviewsService;
    this._storageService = storageService;
    this._machineLearningService = machineLearningService;
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
    } = request.payload;

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

    const testHistoryId = await this._interviewsService.addAnswerByInterviewId({
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
    });

    return {
      success: true,
      message: 'Jawaban berhasil disimpan',
      data: {
        testHistoryId,
      },
    };
  }
}

module.exports = InterviewsHandler;
