const fs = require('fs');
const tf = require('@tensorflow/tfjs-node');
const handler = tf.io.fileSystem('./model/model.json');
const model = tf.loadLayersModel(handler)
const tokenizerJson = fs.readFileSync('../tokenizer_dict_structure.json', 'utf8');
const tokenizer = JSON.parse(tokenizerJson)

function tokenize(text, max_length) {
  text = text.toLowerCase();
  text = text.replace(/[!"#$%&()*+,-./:;<=>?@\[\\\]\^_`{|}~\t\n]/g, '')
  var split_text = text.split(' ');
  var tokens = [];
  split_text = split_text.slice(0, max_length)

  split_text.forEach(element => {
    if (tokenizer[element] != undefined) {
      tokens.push(tokenizer[element]);
    }
  });
  let i = 0;
  tokenized_text_segments = [];
  while (i + 50 < Math.max(tokens.length, 100)) {
    var new_slice = tokens.slice(i, i + 100);
    while (new_slice.length < max_length) {
      new_slice.push(0);
    }
    tokenized_text_segments.push(new_slice);
    i = i + 50;
  }
  return tokenized_text_segments;
}

async function predictText(answer) {
  model.then(function (res) {
    var x = tokenize(answer, 10)
    x = res.predict(tf.tensor2d(x, [x.length, 10]))
    x = tf.mean(x)
    x = x.arraySync();
    if (x >= 0.5) {
      console.log('BAGUS')
    } else {
      console.log('KURANG BAGUS')
    }
  }, function (err) {
    console.log(err);
  });
}

const answerUser = 'saya biasanya membuat daftar prioritas untuk menentukan tindakan yang paling mendesak.'
const answerUser2 = 'Ketika saya dihadapkan pada masalah, saya biasanya membuat daftar prioritas untuk menentukan tindakan yang paling mendesak.'

predictText(answerUser)
predictText(answerUser2)