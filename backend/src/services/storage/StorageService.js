const { Storage } = require('@google-cloud/storage');
const { format } = require('util');

const InvariantError = require('../../exceptions/InvariantError');

class StorageService {
  constructor() {
    let storage;
    if (process.env.APP_ENV === 'dev') {
      storage = new Storage({ keyFilename: 'fake.json' });
    } else {
      storage = new Storage({ keyFilename: 'google-cloud-key.json' });
    }
    const bucket = storage.bucket('woven-honor-403104-interviewku-audio');

    this._bucket = bucket;
  }

  async saveToCloudStorage(audio, userId) {
    // TODO save to cloud storage bucket
    if (!audio) {
      throw new InvariantError('No file uploaded.');
    }

    const filename = `public/${+new Date()}-${userId}-${audio.filename}`;

    const options = {
      destination: filename,
      // Optional:
      // Set a generation-match precondition to avoid potential race conditions
      // and data corruptions. The request to upload is aborted if the object's
      // generation number does not match your precondition. For a destination
      // object that does not yet exist, set the ifGenerationMatch precondition to 0
      // If the destination object already exists in your bucket, set instead a
      // generation-match precondition using its generation number.
      // preconditionOpts: { ifGenerationMatch: generationMatchPrecondition },
    };

    // const uploadResponse = await this._bucket.upload(audio.path, options);

    // console.log(uploadResponse);

    const audioUrl = format(
      `https://storage.googleapis.com/${this._bucket.name}/${filename}`,
    );

    // return audioUrl;
    return 'TODO return audioUrl';
  }
}

module.exports = StorageService;
