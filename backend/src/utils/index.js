function dateFromDBToRightFormatDate(date) {
  return date === null ? null : `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
}

function utcToLocalTimeZone(utcTime) {
  return utcTime === null ? null : utcTime.toString();
}

module.exports = {
  dateFromDBToRightFormatDate,
  utcToLocalTimeZone,
};
