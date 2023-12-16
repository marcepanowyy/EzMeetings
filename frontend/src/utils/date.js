export function formatDateToISO(date) {
    return date.toISOString().split(':')[0]; // Usuwa sekundy i milisekundy
  }