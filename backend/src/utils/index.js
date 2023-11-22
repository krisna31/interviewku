/* eslint-disable no-nested-ternary */
function dateFromDBToRightFormatDate(date) {
  return date === null ? null : `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
}

function utcToLocalTimeZone(utcTime) {
  return utcTime === null ? null : utcTime.toString();
}

function randomInRange(start, end) {
  return process.env.APP_ENV === 'dev' ? 3 : Math.floor(Math.random() * (end - start + 1) + start);
}

function getFeedback(scoreParam, strukturs, repeat) {
  const summary = [
    'Dari jawaban yang kamu berikan kurang bagus',
    'Dari jawaban yang kamu berikan lumayan bagus',
    'Dari jawaban yang kamu berikan cukup bagus',
    'Dari jawaban yang kamu berikan sudah bagus',
    'Dari jawaban yang kamu berikan sudah sangat bagus',
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
    'tingkat fokus kamu dan memahami pertanyaan dalam melakukan sesi interview sudah baik',
    'tingkat fokus kamu dalam melakukan sesi interview cukup baik',
    'sebaiknya kamu lebih fokus lagi dalam melakukan sesi interview agar bisa memahami pertanyaan yang diberikan',
  ];

  const feedback = [];

  // processing data
  const score = Math.round((scoreParam * 100) / 20);

  const xrepeat = repeat === 0 ? 0
    : repeat < 3 ? 1 : 2;

  let total = score
  + (strukturs === 0 ? 1 : 5)
  + (xrepeat === 0 ? 5 : xrepeat === 1 ? 3 : 1);

  total = total < 4 ? 0
    : total < 7 ? 1
      : total < 10 ? 2
        : total < 13 ? 3
          : total < 16 ? 4 : 4;

  // scoring
  feedback.push(summary[total], scoring[score], struktur[strukturs], `dan ${repeatArray[xrepeat]}`);
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

module.exports = {
  dateFromDBToRightFormatDate,
  utcToLocalTimeZone,
  randomInRange,
  getFeedback,
  sendCustomResponseByStatusCode,
  getDateAfterXMinutes,
};
