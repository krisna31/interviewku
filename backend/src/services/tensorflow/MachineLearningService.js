const { Pool } = require('pg');

class MachineLearningService {
  constructor() {
    this._pool = new Pool();
  }

  async getScore({ userAnswer }) {
    // TODO get score from machine learning
    return 0.5;
  }

  async getFeedback({ score, question }) {
    // TODO get feedback from machine learning
    return 1;
  }
}

module.exports = MachineLearningService;
