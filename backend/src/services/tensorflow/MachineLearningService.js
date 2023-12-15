/* eslint-disable no-continue */
/* eslint-disable class-methods-use-this */
/* eslint-disable no-restricted-syntax */
const fs = require('fs');
const tf = require('@tensorflow/tfjs-node');

class MachineLearningService {
  constructor() {
    // common_words
    this._commonWords = JSON.parse(fs.readFileSync(`${__dirname}/common_words.json`, 'utf8'));
    // Sentences Structure's model and tokenizer
    this._strukturModel = tf.loadLayersModel(tf.io.fileSystem(`${__dirname}/tfjs_model/struktur_model/model.json`));
    this._strukturModelTokenizer = JSON.parse(fs.readFileSync(`${__dirname}/tfjs_model/struktur_model/tokenizer_dict.json`, 'utf8'));

    // Field Classification's model and tokenizer
    this._similarityModel = tf.loadLayersModel(tf.io.fileSystem(`${__dirname}/tfjs_model/similarity_model/model.json`));
    this._similarityModelTokenizer = JSON.parse(fs.readFileSync(`${__dirname}/tfjs_model/similarity_model/tokenizer_dict.json`, 'utf8'));

    // Chatbot's model, tokenizer, and tags class
    this._chatbotModel = tf.loadLayersModel(tf.io.fileSystem(`${__dirname}/tfjs_model/chatbot_model/model.json`));
    this._chatbotModelTokenizer = JSON.parse(fs.readFileSync(`${__dirname}/tfjs_model/chatbot_model/tokenizer_dict_chatbot.json`, 'utf8'));
    this._chatbotModelTagsClassJson = JSON.parse(fs.readFileSync(`${__dirname}/tfjs_model/chatbot_model/result_decoder.json`, 'utf8'));
    this._chatbotModelDatasetJson = JSON.parse(fs.readFileSync(`${__dirname}/tfjs_model/chatbot_model/dataset_training.json`, 'utf8'));

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

    this._blacklistWord = ['saya'];

    this._replaceWords = [
      ['wawancara', ['interview']],
      ['online', ['daring', 'remote']],
      ['meeting', ['gmeet', 'zoom']],
      ['persiapan', ['persiapkan', 'disiapkan', 'dipersiapkan']],
      ['cv', ['curiculum vitae', 'resume']],
      ['penutup', ['closing statement']],
      ['mereschedule', ['jadwal ulang', 'menjadwalkan ulang', 'mengubah jadwal', 'merubah jadwal', 'pindah jadwal']],
      ['pewawancara', ['hrd', 'recruiter', 'interviewer', 'hr']],
      ['bagus', ['menarik', 'keren', 'tepat']],
      ['kesalahan umum', ['kesalahan kecil']],
      ['pakaian', ['baju', 'setelan', 'kostum', 'berpenampilan']],
      ['stres', ['stress', 'pusing', 'pening', 'bingung']],
      ['bahasa tubuh', ['gestur', 'gerak tubuh', 'postur']],
      ['kurang', ['minim']],
      ['gugup', ['terbatabata', 'gagap', 'grogi']],
      ['saat', ['dalam proses']],
      ['pekerjaan', ['job', 'kerjaan']],
      ['hai', ['hello', 'hy', 'helo', 'halo', 'hay', 'p']],
      ['lowongan kerja', ['loker', 'lowker']],
    ];
  }

  async getAnswerFromQuestion(question) {
    return new Promise((resolve, reject) => {
      this._chatbotModel.then((res) => {
        // preprocessing text
        const processedText = this.chatbotPreprocessingText(question);
        const tokenizedText = this.tokenize(processedText, this._chatbotModelTokenizer, 10);

        // predict text
        const predictResult = res.predict(tf.tensor2d(tokenizedText));
        const tagResultSequence = predictResult.argMax(-1).arraySync()[0];
        const tagResult = this.getKeyByValue(this._chatbotModelTagsClassJson, tagResultSequence);

        const repsonses = this._chatbotModelDatasetJson
          .filter((d) => d.tag === tagResult)[0].responses;
        const response = repsonses[Math.floor(Math.random() * repsonses.length)];

        resolve(response);
      }).catch((err) => {
        console.error('🚀 ~ file: MachineLearningService.js:94 ~ MachineLearningService ~ this._chatbotModel.then ~ err:', err);
        reject(err);
      });
    });
  }

  chatbotPreprocessingText(sentence) {
    const filteredWords = sentence.toLowerCase().replace(/[^\w\d\s]/g, '');
    const words = filteredWords.split(/\s+/);

    const cleanedWords = [];
    for (const word of words) {
      if (this._blacklistWord.includes(word)) continue;
      let replaced = false;
      for (const [replacement, target] of this._replaceWords) {
        if (target.includes(word)) {
          cleanedWords.push(replacement);
          replaced = true;
        }
      }
      if (!replaced) {
        cleanedWords.push(word);
      }
    }

    return cleanedWords.join(' ');
  }

  async getScore({
    userAnswer, field, allAnswer, retryAttempt, strukturScore,
  }) {
    if (userAnswer == null) {
      return null;
    }

    // field classification from user's answer
    const predictField = await this.predictText({ userAnswer });
    let resultField = false;

    if (predictField === field) {
      resultField = true;
    }

    // calculating similarity between user's answer and dataset
    const listScore = [];
    for (let i = 0; i < allAnswer.length; i += 1) {
      listScore.push(this.cosineSimilarity(userAnswer, allAnswer[i].answer));
    }
    const resultSimilarity = Math.max(...listScore);
    let buffedSimilarity;
    if (resultSimilarity > 0.8) {
      buffedSimilarity = 1;
    } else if (resultSimilarity > 0.6) {
      buffedSimilarity = 0.75;
    } else if (resultSimilarity > 0.4) {
      buffedSimilarity = 0.5;
    } else if (resultSimilarity > 0.2) {
      buffedSimilarity = 0.25;
    } else {
      buffedSimilarity = 0;
    }

    // Final Score composition is:
    // 15% sentences structure, 5% retry attempt, 40% field classification, dan 40% similarity
    let retryScore;

    if (retryAttempt === 0) {
      retryScore = 1;
    } else if (retryAttempt < 3) {
      retryScore = 0.75;
    } else if (retryAttempt < 5) {
      retryScore = 0.5;
    } else if (retryAttempt < 7) {
      retryScore = 0.25;
    } else {
      retryScore = 0;
    }

    // eslint-disable-next-line max-len
    const finalScore = (strukturScore * 0.15) + (retryScore * 0.05) + (resultField * 0.4) + (buffedSimilarity * 0.4);

    return finalScore >= 1 ? 1 : finalScore;
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
        console.error('🚀 ~ file: MachineLearningService.js:143 ~ MachineLearningService ~ this._similarityModel.then ~ err:', err);
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

      return tokenizeUserAnswer >= 1 ? 1 : tokenizeUserAnswer;
    } catch (err) {
      console.error('🚀 ~ file: MachineLearningService.js:168 ~ MachineLearningService ~ getStrukturScore ~ err:', err);
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
    for (let i = 0; i < arr1.length; i += 1) {
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
    for (let i = 0; i < commonWords.length; i += 1) {
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
        count1 += 1;
      }
    }

    for (const element of arr2a) {
      if (!set1.has(element)) {
        count2 += 1;
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
    for (let i = 0; i < arr1c.length; i += 1) {
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
    for (let i = 0; i < D1.length; i += 1) {
      D1D2 += D1[i] * D2[i];
    }

    // calculate similarity = D1.D2 / (||D1|| * ||D2||)
    const similarity = D1D2 / (Math.sqrt(D1Sum) * Math.sqrt(D2Sum));
    return Number(similarity.toFixed(2));
  }
}

module.exports = MachineLearningService;
