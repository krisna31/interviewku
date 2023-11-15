const { Pool } = require('pg');

class StorageService {
  constructor() {
    this._pool = new Pool();
  }

  async saveToCloudStorage(audio) {
    // TODO save to cloud storage bucket

    const audioUrl = 'TODO save to cloud storage bucket';
    const userAnswer = 'TODO save to cloud storage bucket';
    const duration = 12345;

    return {
      audioUrl,
      userAnswer,
      duration,
    };
  }
}

module.exports = StorageService;
