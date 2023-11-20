const { getAudioDurationInSeconds } = require('get-audio-duration');
const InvariantError = require('../../exceptions/InvariantError');

class AudioService {
  constructor() {
    this._getDurationService = getAudioDurationInSeconds;
  }

  async validateAudio(audio) {
    if (!audio) {
      throw new InvariantError('Audio tidak ditemukan');
    }

    const duration = await this._getDurationService(audio.path);

    if (duration > 120) {
      throw new InvariantError('Durasi audio tidak boleh lebih dari 2 menit');
    }

    return duration;
  }

  async convertAudioToText(audio) {
    // TODO speech to text
    return 'TODO speech to text';
  }
}

module.exports = AudioService;
