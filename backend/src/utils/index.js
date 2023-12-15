/* eslint-disable no-nested-ternary */
function dateFromDBToRightFormatDate(date) {
  return date === null ? null : `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${(date.getDate()).toString().padStart(2, '0')}`;
}

// function utcToLocalTimeZone(utcTime) {
//   return utcTime === null ? null : utcTime.toString();
// }

function randomInRange(start, end) {
  return process.env.APP_ENV === 'dev' ? 3 : Math.floor(Math.random() * (end - start + 1) + start);
}

function strukturScoreToFeedback(strukturScore) {
  return strukturScore >= 0.5 ? 'Bagus' : 'Kurang Bagus';
}

function getFeedback(scoreParam, strukturs, repeat) {
  if (scoreParam === null || strukturs === null || repeat === null) {
    return '-';
  }

  const summary = [
    'Performa kamu sedikit kurang bagus',
    'Performa kamu cukup bagus',
    'Performa kamu lumayan bagus',
    'Performa kamu sudah bagus',
    'Performa kamu sudah sangat bagus',
  ];

  const scoring = [
    'tingkat relatif jawaban dengan pertanyaan yang diberikan tidak tepat',
    'tingkat relatif jawaban dengan pertanyaan yang diberikan kurang tepat',
    'tingkat relatif jawaban dengan pertanyaan yang diberikan cukup tepat',
    'tingkat relatif jawaban dengan pertanyaan yang diberikan sudah tepat',
    'tingkat relatif jawaban dengan pertanyaan yang diberikan sudah sangat tepat',
  ];

  const struktur = [
    'namun penyampaian yang kamu berikan kurang tepat',
    'penyampaian yang kamu berikan mudah dipahami',
  ];

  const repeatArray = [
    'tingkat fokus kamu dalam memahami pertanyaan saat melakukan simulasi sudah baik',
    'tingkat fokus kamu dalam melakukan simulasi cukup baik',
    'sebaiknya kamu lebih fokus lagi dalam melakukan simulasi agar bisa memahami pertanyaan yang diberikan',
  ];

  const feedback = [];

  // processing data
  const score = Math.round((scoreParam * 100) / 20);

  const xrepeat = repeat === 0 ? 0
    : repeat < 3 ? 1 : 2;

  const struktursScoreIndex = strukturs < 0.5 ? 0 : 1;

  let total = score
    + (struktursScoreIndex === 0 ? 1 : 5)
    + (xrepeat === 0 ? 5 : xrepeat === 1 ? 3 : 1);

  total = total < 4 ? 0
    : total < 7 ? 1
      : total < 10 ? 2
        : total < 13 ? 3
          : total < 16 ? 4 : 4;

  // scoring
  feedback.push(summary[total], scoring[score], struktur[struktursScoreIndex], `dan ${repeatArray[xrepeat]}`);
  return `${feedback.join(', ')}.`;
}

function sendCustomResponseByStatusCode(response, h, code, message) {
  if (process.env.APP_ENV === 'dev') {
    return h.response({
      success: false,
      message: `${message} - ${response.message}`,
    }).code(code);
  }

  return h.response({
    success: false,
    message,
  }).code(code);
}

function getDateAfterXMinutes(date, minutes) {
  return new Date(date.getTime() + minutes * 60000);
}

function changeToOneUntilFiveRange(number) {
  if (number === null) return null;
  const result = Math.round(number * 5);

  if (result >= 5) return 5;

  return result <= 1 ? 1 : result;
}

function utcToLocalTimeZone(date) {
  if (date === null) return null;

  date.setHours(date.getHours() + 7);

  return date.toLocaleString('id-ID', {
    day: '2-digit',
    month: 'long',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

module.exports = {
  dateFromDBToRightFormatDate,
  utcToLocalTimeZone,
  randomInRange,
  getFeedback,
  sendCustomResponseByStatusCode,
  getDateAfterXMinutes,
  strukturScoreToFeedback,
  changeToOneUntilFiveRange,
};
