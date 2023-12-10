/* eslint-disable no-restricted-syntax */
/* eslint-disable max-len */
/* eslint-disable no-plusplus */
/* eslint-disable class-methods-use-this */
const fs = require('fs');
const tf = require('@tensorflow/tfjs-node');

class MachineLearningService {
  constructor() {
    this._commonWords = JSON.parse(fs.readFileSync(`${__dirname}/common_words.json`, 'utf8'));
    this._strukturModel = tf.loadLayersModel(tf.io.fileSystem(`${__dirname}/tfjs_model/struktur_model/model.json`));
    this._strukturModelTokenizer = JSON.parse(fs.readFileSync(`${__dirname}/tfjs_model/struktur_model/tokenizer_dict.json`, 'utf8'));
    this._similarityModel = tf.loadLayersModel(tf.io.fileSystem(`${__dirname}/tfjs_model/similarity_model/model.json`));
    this._similarityModelTokenizer = JSON.parse(fs.readFileSync(`${__dirname}/tfjs_model/similarity_model/tokenizer_dict.json`, 'utf8'));

    this._questionClass = {
      'General - Tentang Perusahaan': 0,
      'General - Latar Belakang User': 1,
      'General - Terkait Posisi': 2,
      'General - Softskill Situasional': 3,
      'General - Penutup': 4,
      'Kuliner dan Restoran': 5,
      'Penjualan dan Pemasaran': 6,
      'Layanan Pelanggan': 7,
      'Administrasi dan Akuntansi': 8,
      'Desain Grafis': 9,
      IT: 10,
      Keamanan: 11,
      Kesehatan: 12,
      Pendidikan: 13,
      Hiburan: 14,
      Perhotelan: 15,
    };
  }

  async getAnswerFromQuestion(question) {
    // TODO: get and return answer

    return 'answer';
  }

  async getScore({
    userAnswer, field, allAnswer,
  }) {
    if (userAnswer == null) {
      return null;
    }

    // melakukan predict field
    const predictField = await this.predictText({ userAnswer });
    let resultField = false;

    if (predictField === field) {
      resultField = true;
    }

    // menghitung similarity jawaban dari membandingkan jawaban user ke jawaban dataset
    const listScore = [];
    for (let i = 0; i < allAnswer.length; i++) {
      listScore.push(this.cosineSimilarity(userAnswer, allAnswer[i].answer));
    }
    const resultSimilarity = Math.max(...listScore);

    console.log('field jawaban:', predictField);
    console.log('similarity:', resultSimilarity);

    // Komposisi score akhir yaitu 70% field dan 30% similarity
    const finalScore = (resultField * 0.7) + (resultSimilarity * 0.3);
    console.log('final score:', finalScore);

    return finalScore;
  }

  async predictText({ userAnswer }) {
    return new Promise((resolve, reject) => {
      this._similarityModel.then((res) => {
        // preprocessing text
        const processedText = this.preprocessingText(userAnswer, 25, this._commonWords);
        const tokenizedText = this.tokenize(processedText, this._similarityModelTokenizer, 25);

        // predict text
        const similarity = res.predict(tf.tensor2d(tokenizedText));
        const result = similarity.argMax(-1).arraySync()[0];
        resolve(this.getKeyByValue(this._questionClass, result));
      }).catch((err) => {
        reject(err);
      });
    });
  }

  async getStrukturScore({ userAnswer }) {
    if (userAnswer == null) {
      return null;
    }

    try {
      const res = await this._strukturModel;
      let tokenizeUserAnswer = this.tokenize(userAnswer, this._strukturModelTokenizer, 10);
      tokenizeUserAnswer = res.predict(
        tf.tensor2d(
          tokenizeUserAnswer,
          [tokenizeUserAnswer.length, 10],
        ),
      );
      tokenizeUserAnswer = tf.mean(tokenizeUserAnswer);
      tokenizeUserAnswer = tokenizeUserAnswer.arraySync();

      return tokenizeUserAnswer;
    } catch (err) {
      console.log(err);
      throw new Error(`Error loading model: ${err}`);
    }
  }

  tokenize(text, tokenizer, maxLength) {
    let splitText = text
      .toLowerCase()
      .replace(/[!"#$%&()*+,-./:;<=>?@[\\\]^_`{|}~\t\n]/g, '')
      .split(' ');

    const tokens = [];
    splitText = splitText.slice(0, maxLength);

    splitText.forEach((element) => {
      if (tokenizer[element] !== undefined) {
        tokens.push(tokenizer[element]);
      }
    });
    let i = 0;
    const tokenizedTextSegments = [];
    while (i + 50 < Math.max(tokens.length, 100)) {
      const newSlice = tokens.slice(i, i + 100);
      while (newSlice.length < maxLength) {
        newSlice.push(0);
      }
      tokenizedTextSegments.push(newSlice);
      i += 50;
    }
    return tokenizedTextSegments;
  }

  reverseNonSimilar(arr1, arr2) {
    const similar = [];
    const nonsimilar = [];
    for (let i = 0; i < arr1.length; i++) {
      if (arr1[i] === arr2[i] && arr1[i] !== null) {
        similar.push(arr2[i]);
        // eslint-disable-next-line no-continue
        continue;
      }
      nonsimilar.push(arr2[i]);
    }
    nonsimilar.reverse();
    return similar.concat(nonsimilar);
  }

  preprocessingText(sentence, maxLength, commonWords) {
    const filteredWords = sentence.replace(/[^\w\d\s]/g, '').split(/\s+/);
    let words = filteredWords.filter((word) => word.length > 0);
    let prevLen = words.length;
    for (let i = 0; i < commonWords.length; i++) {
      if (words.length <= maxLength) break;
      words = words.filter((word) => word.toLowerCase() !== commonWords[i].word);
      if (words.length === prevLen) break;
      prevLen = words.length;
    }

    return words.slice(0, maxLength).join(' ');
  }

  getKeyByValue(object, targetValue) {
    // eslint-disable-next-line no-restricted-syntax
    for (const key in object) {
      // eslint-disable-next-line no-prototype-builtins
      if (object.hasOwnProperty(key) && object[key] === targetValue) {
        return key;
      }
    }
    return null;
  }

  cosineSimilarity(textA, textB) {
    const arr1 = textA.split(' ');
    const arr2 = textB.split(' ');

    // sorting same element and move it into the front
    const commonElements = arr1.filter((element) => arr2.includes(element)).sort();
    const uniqueArr1 = arr1.filter((element) => !arr2.includes(element));
    const uniqueArr2 = arr2.filter((element) => !arr1.includes(element));
    const arr1a = [...commonElements, ...uniqueArr1];
    const arr2a = [...commonElements, ...uniqueArr2];
    const totalSameWord = arr1.length - uniqueArr1.length;

    // padding null to the end of array until the size of both array is equal
    const set1 = new Set(arr1a);
    const set2 = new Set(arr2a);
    let count1 = 0;
    let count2 = 0;
    for (const element of arr1a) {
      if (!set2.has(element)) {
        count1++;
      }
    }

    for (const element of arr2a) {
      if (!set1.has(element)) {
        count2++;
      }
    }
    const arr1b = [...arr1a].concat(new Array(count2).fill(null));
    const arr2b = [...arr2a].concat(new Array(count1).fill(null));

    // moving element that have same index with null by reverse the second array
    const arr1c = [...arr1b];
    const arr2c = this.reverseNonSimilar(arr1b, arr2b);

    // calculate magnitude of the vector, ||D1|| = sqrt(D1^2 + D2^2 + ... + Dn^2)
    const D1 = [];
    const D2 = [];
    for (let i = 0; i < arr1c.length; i++) {
      if (i < totalSameWord) {
        D1.push(1); D2.push(1);
      } else if (arr1c[i] === null) {
        D1.push(0); D2.push(1);
      } else if (arr2c[i] === null) {
        D1.push(1); D2.push(0);
      }
    }

    // calculate D1.D2 = D1[0] * D2[0] + D1[1] * D2[1] + ... + D1[n] * D2[n]
    let D1D2 = 0;
    const D1Sum = D1.reduce((a, b) => a + b, 0);
    const D2Sum = D2.reduce((a, b) => a + b, 0);
    for (let i = 0; i < D1.length; i++) {
      D1D2 += D1[i] * D2[i];
    }

    // calculate similarity = D1.D2 / (||D1|| * ||D2||)
    const similarity = D1D2 / (Math.sqrt(D1Sum) * Math.sqrt(D2Sum));
    return Number(similarity.toFixed(2));
  }
}

module.exports = MachineLearningService;
