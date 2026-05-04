/**
 * API implementation using real network requests
 */

const BASE_URL = window.API_BASE_URL || '';

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
  async getReservations() {
    const response = await fetch(`${BASE_URL}/reservations`);
    return handleResponse(response);
  },

  async getReservableTimes(date) {
    const [times, reservations] = await Promise.all([
      this.getTimes(),
      this.getReservations()
    ]);

    const reservedTimeIds = new Set(
      reservations
        .filter(reservation => reservation.date === date)
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
        timeId: Number(data.timeId)
      })
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
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    return handleResponse(response);
  },

  async deleteTheme(id) {
    const response = await fetch(`${BASE_URL}/themes/${id}`, {
      method: 'DELETE'
    });
    return handleResponse(response);
  }
};
