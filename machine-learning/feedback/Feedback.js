const summary =
  [
    'Dari jawaban yang kamu berikan kurang bagus',
    'Dari jawaban yang kamu berikan lumayan bagus',
    'Dari jawaban yang kamu berikan cukup bagus',
    'Dari jawaban yang kamu berikan sudah bagus',
    'Dari jawaban yang kamu berikan sudah sangat bagus',
  ]

const scoring =
  [
    'tingkat relatif jawaban dengan pertanyaan yang diberikan tidak tepat',
    'tingkat relatif jawaban dengan pertanyaan yang diberikan kurang tepat',
    'tingkat relatif jawaban dengan pertanyaan yang diberikan cukup tepat',
    'tingkat relatif jawaban dengan pertanyaan yang diberikan sudah tepat',
    'tingkat relatif jawaban dengan pertanyaan yang diberikan sudah sangat tepat',
  ]

const struktur =
  [
    'namun penyampaian yang kamu berikan kurang tepat',
    'penyampaian yang kamu berikan mudah dipahami',
  ]

const repeat =
  [
    'tingkat fokus kamu dalam memahami pertanyaan saat melakukan simulasi sudah baik',
    'tingkat fokus kamu dalam melakukan simulasi cukup baik',
    'sebaiknya kamu lebih fokus lagi dalam melakukan simulasi agar bisa memahami pertanyaan yang diberikan',
  ]

/*
Kalimat terdiri dari 4 penggalan kata, yaitu:
1. Summary dari ketiga indeks, tiap indeks memiliki nilai maks 5
2. Scoring, skalanya 0 - 5
3. Struktur kalimat, terdiri dari 2 inputan (0 dan 1)
4. X Repeat, inputan berupa integer dengan 3 patokan penilaian
*/

// example
// Scoring input = 0 - 1
// Struktur Kalimat input = 0 atau 1
// X Repeat input = 0 - n
let score = 0.5
let strukturs = 1
let xrepeat = 3

let feedback = []

// processing data
score = Math.round(score * 100 / 20)

xrepeat =
  xrepeat == 0 ? 0 :
    xrepeat < 3 ? 1 : 2

let total =
  score +
  (strukturs == 0 ? 1 : 5) +
  (xrepeat == 0 ? 5 : xrepeat == 1 ? 3 : 1)

total =
  total < 4 ? 0 :
    total < 7 ? 1 :
      total < 10 ? 2 :
        total < 13 ? 3 :
          total < 16 ? 4 : 4

// scoring
feedback.push(summary[total], scoring[score], struktur[strukturs], "dan " + repeat[xrepeat])
console.log(feedback.join(', ') + '.');
