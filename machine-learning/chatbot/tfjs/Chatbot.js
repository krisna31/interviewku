const tf = require('@tensorflow/tfjs-node');
const fs = require('fs');
const handler = tf.io.fileSystem('./model/model.json');
const model = tf.loadLayersModel(handler);
const tokenizerJson = fs.readFileSync('../tokenizer_dict_chatbot.json', 'utf8');
const tokenizer = JSON.parse(tokenizerJson);
const tagsClassJson = fs.readFileSync('../chatbot_result_decoder.json', 'utf8');
const tagsClass = JSON.parse(tagsClassJson);
const responsesJson = fs.readFileSync('../responses_chatbot.json', 'utf8');
const responses = JSON.parse(responsesJson);


const replace_words = [
  ['online', ['daring', 'remote']],
  ['meeting', ['gmeet', 'zoom']],
  ['persiapan', ['persiapkan', 'disiapkan', 'dipersiapkan']],
  ['cv', ['curiculum vitae', 'resume']],
  ['penutup', ['closing statement']],
  ['mereschedule', ['jadwal ulang', 'menjadwalkan ulang', 'mengubah jadwal', 'merubah jadwal', 'pindah jadwal']],
  ['pewawancara', ['hrd']],
  ['bagus', ['menarik', 'keren', 'tepat']],
  ['kesalahan umum', ['kesalahan kecil']],
  ['saran', ['tips']],
  ['pakaian', ['baju', 'setelan', 'kostum']]
];

function preprocessingText(sentence) {
  const filteredWords = sentence.toLowerCase().replace(/[^\w\d\s]/g, '');
  const words = filteredWords.split(/\s+/);

  const cleanedWords = [];
  for (let word of words) {
    let replaced = false;
    for (let [replacement, target] of replace_words) {
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

function getKeyByValue(object, targetValue) {
  for (let key in object) {
    if (object.hasOwnProperty(key) && object[key] === targetValue) {
      return key;
    }
  }
  return null;
}

function tokenize(text, max_length) {
  text = text.toLowerCase();
  text = text.replace(/[!"#$%&()*+,-./:;<=>?@\[\\\]\^_`{|}~\t\n]/g, '')
  let split_text = text.split(' ');
  let tokens = [];
  split_text = split_text.slice(0, max_length)

  split_text.forEach(element => {
    if (tokenizer[element] != undefined) {
      tokens.push(tokenizer[element]);
    }
  });
  let i = 0;
  tokenized_text_segments = [];
  while (i + 50 < Math.max(tokens.length, 100)) {
    let new_slice = tokens.slice(i, i + 100);
    while (new_slice.length < max_length) {
      new_slice.push(0);
    }
    tokenized_text_segments.push(new_slice);
    i = i + 50;
  }
  // console.log(tokenized_text_segments);

  return tokenized_text_segments;
}

async function predictText(answer) {
  return new Promise((resolve, reject) => {
    model.then(function (res) {
      // preprocessing text
      const processedText = preprocessingText(answer);
      const tokenizedText = tokenize(processedText, 13);

      // predict text
      const predictResult = res.predict(tf.tensor2d(tokenizedText));
      const tagResultSequence = predictResult.argMax(-1).arraySync()[0];
      const tagResult = getKeyByValue(tagsClass, tagResultSequence);

      const resultList = responses[tagResult];
      const randomIndex = Math.floor(Math.random() * resultList.length);
      const result = resultList[randomIndex];

      resolve(result);
    }).catch(function (err) {
      reject(err);
    });
  });
}

const main = async () => {
  const result = await predictText('apa yang harus disiapkan sebelum melakukan wawancara');
  console.log(result);
}

main();

