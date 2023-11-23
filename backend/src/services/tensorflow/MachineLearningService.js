const fs = require('fs');
const tf = require('@tensorflow/tfjs-node');

class MachineLearningService {
  constructor() {
    this._tokenizer = JSON.parse(fs.readFileSync(`${__dirname}/tokenizer_dict.json`, 'utf8'));
    this._model = tf.loadLayersModel(tf.io.fileSystem(`${__dirname}/tfjs_model/model.json`));
  }

  async getScore({ userAnswer }) {
    // TODO get score from machine learning
    return 0.5;
  }

  async getStrukturScore({ userAnswer }) {
    if (userAnswer == null) {
      return null;
    }

    try {
      const res = await this._model;
      let tokenizeUserAnswer = this.tokenize(userAnswer);
      tokenizeUserAnswer = res.predict(
        tf.tensor2d(
          tokenizeUserAnswer,
          [tokenizeUserAnswer.length, 10],
        ),
      );
      tokenizeUserAnswer = tf.mean(tokenizeUserAnswer);
      tokenizeUserAnswer = tokenizeUserAnswer.arraySync();

      console.log(tokenizeUserAnswer);

      console.log(tokenizeUserAnswer >= 0.5 ? 'BAGUS' : 'KURANG BAGUS');

      return tokenizeUserAnswer;
    } catch (err) {
      console.log(err);
      throw new Error(`Error loading model: ${err}`);
    }
  }

  tokenize(text) {
    let splitText = text
      .toLowerCase()
      .replace(/[!"#$%&()*+,-./:;<=>?@[\\\]^_`{|}~\t\n]/g, '')
      .split(' ');

    const tokens = [];

    splitText = splitText.slice(0, 10);

    splitText.forEach((element) => {
      if (this._tokenizer[element] !== undefined) {
        tokens.push(this._tokenizer[element]);
      }
    });
    let i = 0;
    const tokenizedTextSegments = [];

    while (i + 50 < Math.max(tokens.length, 100)) {
      const newSlice = tokens.slice(i, i + 100);
      while (newSlice.length < 10) {
        newSlice.push(0);
      }
      tokenizedTextSegments.push(newSlice);
      i += 50;
    }

    return tokenizedTextSegments;
  }
}

module.exports = MachineLearningService;
