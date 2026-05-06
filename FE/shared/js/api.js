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
  if (!response.ok) {
    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
      const error = await response.json().catch(() => ({ message: 'An error occurred' }));
      throw new Error(error.message || 'Network response was not ok');
    }

    const message = await response.text().catch(() => '');
    throw new Error(message || 'Network response was not ok');
  }
  if (response.status === 204) return null;
  return response.json();
};

export const api = {
  // Reservations
  async getReservations({ date, themeId } = {}) {
    const query = new URLSearchParams();
    if (date) query.set('date', date);
    if (themeId) query.set('themeId', themeId);

    const path = query.size > 0 ? `/reservations?${query}` : '/reservations';
    const response = await fetch(`${BASE_URL}${path}`);
    return handleResponse(response);
  },

  async getReservableTimes(date, themeId) {
    const [times, reservations] = await Promise.all([
      this.getTimes(),
      themeId ? this.getReservations({ date, themeId }) : this.getReservations()
    ]);

    const reservedTimeIds = new Set(
      reservations
        .filter(reservation => reservation.date === date)
        .filter(reservation => !themeId || String(reservation.theme?.id) === String(themeId))
        .filter(reservation => reservation.isAvailable === undefined || reservation.isAvailable === false)
        .map(reservation => String(reservation.time.id))
    );

    return times.map(time => ({
      timeId: time.id,
      startAt: time.startAt,
      available: !reservedTimeIds.has(String(time.id))
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

  async deleteReservation(id) {
    const response = await fetch(`${BASE_URL}/reservations/${id}`, {
      method: 'DELETE',
      headers: { Authorization: ADMIN_TOKEN }
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
      sortBy: 'popular',
      from,
      to,
      limit
    });
    const response = await fetch(`${BASE_URL}/themes?${query}`);
    return handleResponse(response);
  }
};
