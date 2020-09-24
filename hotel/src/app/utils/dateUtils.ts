export function dateToJsonDateString(date: Date): string {
  let result: string = date.getFullYear() + '-';
  let month = date.getMonth() + 1;
  if (month.toString().length === 1) {
    result += '0';
  }
  result += month + '-';
  if (date.getDate().toString().length === 1) {
    result += '0';
  }
  result += date.getDate();
  return result;
}
