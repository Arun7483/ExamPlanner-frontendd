export const API_BASE_URL = (() => {
  if (typeof document !== 'undefined') {
    const meta = document.querySelector('meta[name="app-api-url"]');
    const configured = meta?.getAttribute('content');
    if (configured && configured !== '__API_URL__') {
      return configured.replace(/\/$/, '');
    }
  }

  return 'http://localhost:8090';
})();
