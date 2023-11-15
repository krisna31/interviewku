function dateFromDBToRightFormatDate(date) {
  return date === null ? null : `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
}

function utcToLocalTimeZone(utcTime) {
  return utcTime === null ? null : utcTime.toString();
}

function randomInRange(start, end) {
  return Math.floor(Math.random() * (end - start + 1) + start);
}

module.exports = {
  dateFromDBToRightFormatDate,
  utcToLocalTimeZone,
  randomInRange,
};
