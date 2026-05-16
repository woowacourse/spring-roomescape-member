export async function fetchThemes() {
    const res = await fetch('/themes');
    if (!res.ok) throw new Error('Failed to fetch themes');
    const data = await res.json();
    return data.themes || data;
}

export async function fetchRankedThemes(days, limit) {
    const res = await fetch(`/themes/rank?days=${days}&limit=${limit}`);
    if (!res.ok) throw new Error('Failed to fetch ranked themes');
    const data = await res.json();
    return data.themeRankings || data.themes || data;
}

export async function createTheme(payload) {
    const res = await fetch('/themes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error('Failed to create theme');
    return res.json();
}

export async function deleteTheme(id) {
    const res = await fetch(`/themes/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Failed to delete theme');
    return res;
}

export async function fetchReservationTimes() {
    const res = await fetch('/reservation-times');
    if (!res.ok) throw new Error('Failed to fetch times');
    const data = await res.json();
    return data.reservationTimes || data;
}

export async function fetchAvailableTimes(themeId, date) {
    const res = await fetch(`/reservation-times/available?themeId=${themeId}&date=${date}`);
    if (!res.ok) throw new Error('Failed to fetch available times');
    const data = await res.json();
    return data.availableTimes || data;
}

export async function createReservationTime(startAt) {
    const res = await fetch('/reservation-times', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ startAt })
    });
    if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to create time');
    }
    return res.json();
}

export async function deleteReservationTime(id) {
    const res = await fetch(`/reservation-times/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Failed to delete time');
    return res;
}

export async function createReservation(payload) {
    const res = await fetch('/reservations', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to create reservation');
    }
    return res.json();
}

export async function fetchMyReservations(name) {
    const res = await fetch(`/reservations?name=${encodeURIComponent(name)}`);
    if (!res.ok) throw new Error('Failed to fetch my reservations');
    const data = await res.json();
    return data.reservations || data;
}

export async function updateReservationSchedule(id, payload) {
    const res = await fetch(`/reservations/${id}/schedule`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message || 'Failed to update schedule');
    }
    return res.json();
}

export async function cancelMyReservation(id, payload) {
    const res = await fetch(`/reservations/${id}/cancellation`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error('Failed to cancel reservation');
    return res.json();
}

export async function fetchAdminReservations(page = 0, size = 100) {
    const res = await fetch(`/admin/reservations?page=${page}&size=${size}`);
    if (!res.ok) throw new Error('Failed to fetch admin reservations');
    const data = await res.json();
    return data.reservations || data;
}

export async function deleteAdminReservation(id) {
    const res = await fetch(`/admin/reservations/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Failed to delete admin reservation');
    return res;
}
