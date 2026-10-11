const getMetaApiUrl = (): string | null => {
  if (typeof document === 'undefined') {
    return null;
  }

  return document.querySelector('meta[name="app-api-url"]')?.getAttribute('content')?.trim() || null;
};

const configuredApiUrl = getMetaApiUrl();

export const apiConfig = {
  apiBaseUrl:
    configuredApiUrl && !configuredApiUrl.startsWith('__')
      ? configuredApiUrl.replace(/\/$/, '')
      : 'http://localhost:8090',
};
