const { getAudioDurationInSeconds } = require('get-audio-duration');
const speech = require('@google-cloud/speech');
const fs = require('fs').promises;

const InvariantError = require('../../exceptions/InvariantError');

class AudioService {
  constructor() {
    this._getDurationService = getAudioDurationInSeconds;
    this._googleSpeech = process.env.GOOGLE_SERVICE_ACCOUNT_KEY !== 'false'
      ? new speech.SpeechClient({
        keyFilename: process.env.GOOGLE_SERVICE_ACCOUNT_KEY,
      }) : new speech.SpeechClient();
  }

  async validateAudio(audio) {
    if (!audio) {
      throw new InvariantError('Audio tidak ditemukan');
    }

    const duration = await this._getDurationService(audio.path);

    if (duration >= 125) {
      throw new InvariantError('Durasi audio tidak boleh lebih dari 2 menit');
    }

    return duration;
  }

  async convertAudioToText(audio) {
    return process.env.AUDIO_SERVICE === 'true' ? this._googleSpeechToText(audio) : 'DUMMY saya pada umumnya tidak suka seprti namun, menurut saya biasanya membuat daftar prioritas untuk menentukan tindakan yang paling mendesak. DUMMY';
  }

  async _googleSpeechToText(audio) {
    const audioBytes = await fs.readFile(audio.path, { encoding: 'base64' });

    const audioConfig = {
      encoding: 'AMR_WB',
      // encoding: 'OGG_OPUS',
      sampleRateHertz: 16000,
      languageCode: 'id-ID',
      model: 'default',
    };

    const request = {
      audio: {
        content: audioBytes,
      },
      config: audioConfig,
    };

    const [response] = await this._googleSpeech.recognize(request);
    const transcription = response.results
      .map((result) => result.alternatives[0].transcript)
      .join('\n');

    return transcription;
  }
}

module.exports = AudioService;
