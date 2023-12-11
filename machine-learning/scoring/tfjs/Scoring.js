const tf = require('@tensorflow/tfjs-node');
const fs = require('fs');
const handler = tf.io.fileSystem('./model/model.json');
const model = tf.loadLayersModel(handler);
const commonWordsJson = fs.readFileSync('../common_words.json', 'utf8');
const commonWords = JSON.parse(commonWordsJson);
const tokenizerJson = fs.readFileSync('../tokenizer_dict_scoring.json', 'utf8');
const tokenizer = JSON.parse(tokenizerJson)

function reverseNonSimilar(arr1, arr2) {
  let similar = [];
  let nonsimilar = [];
  for (let i = 0; i < arr1.length; i++) {
    if (arr1[i] === arr2[i] && arr1[i] !== null) {
      similar.push(arr2[i]);
      continue;
    }
    nonsimilar.push(arr2[i]);
  }
  nonsimilar.reverse();
  return similar.concat(nonsimilar);
}

function cosine_similarity(textA, textB) {
  const arr1 = textA.split(' ');
  const arr2 = textB.split(' ');

  // sorting same element and move it into the front
  const commonElements = arr1.filter(element => arr2.includes(element)).sort();
  const uniqueArr1 = arr1.filter(element => !arr2.includes(element));
  const uniqueArr2 = arr2.filter(element => !arr1.includes(element));
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
  const arr2c = reverseNonSimilar(arr1b, arr2b);

  // calculate magnitude of the vector, ||D1|| = sqrt(D1^2 + D2^2 + ... + Dn^2)
  let D1 = [];
  let D2 = [];
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

function preprocessingText(sentence, maxLength, commonWords) {
  let filteredWords = sentence.replace(/[^\w\d\s]/g, '').split(/\s+/);
  let words = filteredWords.filter(word => word.length > 0);
  let prevLen = words.length;
  for (let i = 0; i < commonWords.length; i++) {
    if (words.length <= maxLength) break;
    words = words.filter(word => word.toLowerCase() !== commonWords[i].word);
    if (words.length === prevLen) break;
    prevLen = words.length;
  }

  return words.slice(0, maxLength).join(' ');
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
  return tokenized_text_segments;
}

function getKeyByValue(object, targetValue) {
  for (let key in object) {
    if (object.hasOwnProperty(key) && object[key] === targetValue) {
      return key;
    }
  }
  return null;
}

const questionClass = {
  'General - Tentang Perusahaan': 1,
  'General - Latar Belakang User': 2,
  'General - Terkait Posisi': 3,
  'General - Softskill Situasional': 4,
  'Penutup': 5,
  'Kuliner dan Restoran': 6,
  'Penjualan dan Pemasaran': 7,
  'Layanan Pelanggan': 8,
  'Administrasi dan Akuntansi': 9,
  'Desain Grafis': 10,
  'IT': 11,
  'Keamanan': 12,
  'Kesehatan': 13,
  'Pendidikan': 14,
  'Hiburan': 15,
  'Perhotelan': 16,
}

async function predictText(answer) {
  return new Promise((resolve, reject) => {
    model.then(function (res) {
      // preprocessing text
      const processedText = preprocessingText(answer, 25, commonWords);
      const tokenizedText = tokenize(processedText, 25);

      // predict text
      const similarity = res.predict(tf.tensor2d(tokenizedText));
      const result = similarity.argMax(-1).arraySync()[0];
      resolve(getKeyByValue(questionClass, result+1));
    }).catch(function (err) {
      reject(err);
    });
  });
}

const main = async () => {
  // inputan berupa:
  // - pertanyaan yang diajukan (max: 1)
  // - field dari pertanyaan (max: 1)
  // - jawaban dari dataset berdasarkan pertanyaan yg diajukan (-+ 20 jawaban)
  // - jawaban dari user (max: 1)

  // parameter model
  const pertanyaan = 'Bagaimana Anda mengelola ekspektasi pelanggan atau pemangku kepentingan dalam proyek IT?'
  const field = 'IT'

  // melakukan query dari db untuk mendapatkan jawaban dataset
  const jawaban = [
    'Saya mengelola ekspektasi pelanggan atau pemangku kepentingan dalam proyek IT dengan berkomunikasi secara teratur. Saya menyediakan pembaruan berkala tentang perkembangan proyek, hambatan yang mungkin muncul, dan solusi yang sedang saya eksplorasi.',
    'Saya selalu memastikan bahwa tujuan dan lingkup proyek IT telah ditetapkan secara jelas sejak awal. Dengan cara ini, ekspektasi pelanggan atau pemangku kepentingan lebih mudah dikelola karena mereka tahu apa yang diharapkan.',
    'Ketika ada perubahan dalam rencana atau jadwal proyek, saya segera berkomunikasi dengan pelanggan atau pemangku kepentingan untuk menjelaskan dampaknya dan mencari persetujuan jika perlu.',
    'Saya membentuk tim proyek yang memiliki anggota yang berkomunikasi dengan baik dan dapat berinteraksi dengan pelanggan atau pemangku kepentingan secara efektif. Tim yang komunikatif membantu menjaga ekspektasi tetap realistis.',
    'Saya melibatkan pelanggan atau pemangku kepentingan dalam proses pengambilan keputusan yang relevan untuk proyek. Ini membantu mereka merasa memiliki dan terlibat dalam perjalanan proyek.',
    'Saya menciptakan roadmap proyek yang jelas yang mencakup tonggak penting dan tahapan perkembangan. Ini memberikan panduan yang jelas tentang apa yang dapat diharapkan pelanggan atau pemangku kepentingan dalam waktu tertentu.',
  ]

  // jawaban user
  const jawaban_user = 'Saya selalu menegaskan bahwa tujuan proyek IT sudah ditetapkan dengan jelas sejak awal. Hal ini memudahkan pengelolaan ekspektasi pelanggan atau pemangku kepentingan karena mereka mengetahui harapan yang ada.'

  // melakukan predict field
  const predict_field = await predictText(jawaban_user);

  let result_field = false;
  if (predict_field === field) {
    result_field = true;
  }

  // menghitung similarity jawaban dari membandingkan jawaban user ke jawaban dataset
  let listScore = [];
  for (let i = 0; i < jawaban.length; i++) {
    listScore.push(cosine_similarity(jawaban_user, jawaban[i]));
  }
  let result_similarity = Math.max(...listScore);

  console.log('field jawaban:', predict_field);
  console.log('similarity:', result_similarity);

  // Komposisi score akhir yaitu 70% field dan 30% similarity
  const final_score = (result_field * 0.7) + (result_similarity * 0.3);
  console.log('final score:', final_score);
}

main();