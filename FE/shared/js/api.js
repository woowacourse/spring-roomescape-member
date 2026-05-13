/**
 * API implementation using real network requests
 */

const BASE_URL = window.API_BASE_URL || '';
const ADMIN_TOKEN = 'ADMIN';

export const DEFAULT_THEME_THUMBNAILS = [
  'https://commons.wikimedia.org/wiki/Special:FilePath/Main%20%28Reading%20Room%29%20%283109281787%29.jpg?width=1200',
  'https://commons.wikimedia.org/wiki/Special:FilePath/Hotel%20Hallway.jpeg?width=1200',
  'https://commons.wikimedia.org/wiki/Special:FilePath/Room%20405%2C%20George%20Herbert%20Jones%20Laboratory%2C%20The%20University%20of%20Chicago%20%287189830229%29.jpg?width=1200',
  'https://commons.wikimedia.org/wiki/Special:FilePath/Bunker%20113%20Bernbach.jpg?width=1200',
  'https://commons.wikimedia.org/wiki/Special:FilePath/Passenger%20Compartment%20on%20a%20train%28GN04216%29.jpg?width=1200'
];

export const getThemeThumbnail = (theme = {}, index = 0) => {
  const thumbnail = theme.thumbnail?.trim();
  if (thumbnail) return thumbnail;

  return DEFAULT_THEME_THUMBNAILS[index % DEFAULT_THEME_THUMBNAILS.length];
};

export const getNextThemeThumbnail = (seed = '') => {
  const charTotal = [...seed].reduce((total, char) => total + char.charCodeAt(0), 0);
  return DEFAULT_THEME_THUMBNAILS[charTotal % DEFAULT_THEME_THUMBNAILS.length];
};

const handleResponse = async (response) => {
  const contentType = response.headers.get('content-type') || '';
  const isJson = contentType.includes('application/json');

  if (!response.ok) {
    if (isJson) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || '서버 오류가 발생했습니다.');
    }
    const errorText = await response.text().catch(() => '네트워크 오류가 발생했습니다.');
    throw new Error(errorText);
  }

  if (response.status === 204) return null;

  if (isJson) {
    try {
      const data = await response.json();
      return Array.isArray(data) ? data : data || {};
    } catch (e) {
      return [];
    }
  }

  const text = await response.text();
  // Project-specific: 200 OK with error message in body (text/plain)
  const errorKeywords = ['확인해주세요', '존재합니다', '선택해주세요', '입력해주세요', '권한이 필요합니다', '비어있습니다', '없습니다', '불가능합니다', '불일치'];
  if (errorKeywords.some(keyword => text.includes(keyword)) && text.length < 200) {
    throw new Error(text);
  }
  
  // If it's not JSON and not an error, return empty array as fallback for list methods
  return [];
};

export const api = {
  // Reservations
  async getReservations({ date, themeId, name } = {}) {
    const query = new URLSearchParams();
    if (date) query.set('date', date);
    if (themeId) query.set('themeId', themeId);
    if (name) query.set('name', name);

    const queryString = query.toString();
    const path = queryString ? `/reservations?${queryString}` : '/reservations';
    const response = await fetch(`${BASE_URL}${path}`);
    return handleResponse(response);
  },

  async getReservableTimes(date, themeId) {
    if (!date || !themeId) return [];

    const [allTimes, reservations] = await Promise.all([
      this.getTimes(),
      this.getReservations({ date, themeId })
    ]);

    const bookedTimeIds = new Set(reservations.map(res => String(res.time.id)));

    return allTimes.map(time => ({
      timeId: time.id,
      startAt: time.startAt,
      available: !bookedTimeIds.has(String(time.id))
    }));
  },

  async createReservation(data) {
    const response = await fetch(`${BASE_URL}/reservations`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: data.name,
        date: data.date,
        timeId: Number(data.timeId),
        themeId: Number(data.themeId)
      })
    });
    return handleResponse(response);
  },

  async updateReservation(id, { date, timeId }, name) {
    const response = await fetch(`${BASE_URL}/reservations/${id}?name=${encodeURIComponent(name)}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ date, timeId: timeId ? Number(timeId) : undefined })
    });
    return handleResponse(response);
  },

  async deleteReservation(id) {
    const response = await fetch(`${BASE_URL}/reservations/${id}`, {
      method: 'DELETE',
      headers: { Authorization: ADMIN_TOKEN }
    });
    return handleResponse(response);
  },

  async deleteReservationSelf(id, name) {
    const response = await fetch(`${BASE_URL}/reservations/${id}?name=${encodeURIComponent(name)}`, {
      method: 'DELETE'
    });
    return handleResponse(response);
  },

  // Times
  async getTimes() {
    const response = await fetch(`${BASE_URL}/times`);
    return handleResponse(response);
  },

  async createTime(startAt) {
    const response = await fetch(`${BASE_URL}/times`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ startAt })
    });
    return handleResponse(response);
  },

  async deleteTime(id) {
    const response = await fetch(`${BASE_URL}/times/${id}`, {
      method: 'DELETE'
    });
    return handleResponse(response);
  },

  // Themes
  async getThemes() {
    const response = await fetch(`${BASE_URL}/themes`);
    return handleResponse(response);
  },

  async createTheme(data) {
    const response = await fetch(`${BASE_URL}/themes`, {
      method: 'POST',
      headers: {
        Authorization: ADMIN_TOKEN,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: data.name,
        description: data.description,
        thumbnail: data.thumbnail || getNextThemeThumbnail(data.name)
      })
    });
    return handleResponse(response);
  },

  async deleteTheme(id) {
    const response = await fetch(`${BASE_URL}/themes/${id}`, {
      method: 'DELETE',
      headers: { Authorization: ADMIN_TOKEN }
    });
    return handleResponse(response);
  },

  async getPopularThemes({ from, to, limit = 10 } = {}) {
    const query = new URLSearchParams({
      sortType: 'popularity',
      from,
      to,
      limit
    });
    const response = await fetch(`${BASE_URL}/themes?${query}`);
    return handleResponse(response);
  }
};
