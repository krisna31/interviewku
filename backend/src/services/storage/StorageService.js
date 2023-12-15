const { Storage } = require('@google-cloud/storage');
const InvariantError = require('../../exceptions/InvariantError');

class StorageService {
  constructor() {
    const storage = process.env.GOOGLE_SERVICE_ACCOUNT_KEY !== 'false' ? new Storage({
      keyFilename: process.env.GOOGLE_SERVICE_ACCOUNT_KEY,
    }) : new Storage();
    const bucket = storage.bucket(process.env.AUDIO_BUCKET_NAME);

    this._bucket = bucket;
  }

  async saveToCloudStorage(audio, userId) {
    if (process.env.APP_ENV === 'dev') {
      return audio.path;
    }

    if (!audio) {
      throw new InvariantError('No audio uploaded.');
    }

    const filename = `audio/${+new Date()}-${userId}-${audio.filename}`.replaceAll(' ', '');

    const options = {
      destination: filename,
      // public: true,
      // Optional:
      // Set a generation-match precondition to avoid potential race conditions
      // and data corruptions. The request to upload is aborted if the object's
      // generation number does not match your precondition. For a destination
      // object that does not yet exist, set the ifGenerationMatch precondition to 0
      // If the destination object already exists in your bucket, set instead a
      // generation-match precondition using its generation number.
      // preconditionOpts: { ifGenerationMatch: generationMatchPrecondition },
    };

    const uploadResponse = await this._bucket.upload(audio.path, options);

    if (!uploadResponse) {
      throw new Error('Failed to upload file.');
    }

    return uploadResponse[0].publicUrl();
  }
}

module.exports = StorageService;
