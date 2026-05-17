/* Thin fetch wrappers around the learner's JSON REST API. */
window.api = (function () {
    'use strict';

    async function getJson(url) {
        const res = await fetch(url, {headers: {'Accept': 'application/json'}});
        if (!res.ok) throw await toError(res);
        return res.json();
    }

    async function postJson(url, body) {
        const res = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Accept': 'application/json'},
            body: JSON.stringify(body)
        });
        if (!res.ok) throw await toError(res);
        return res.status === 204 ? null : res.json();
    }

    async function putJson(url, body, headers = {}) {
        const res = await fetch(url, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json', 'Accept': 'application/json', ...headers},
            body: JSON.stringify(body)
        });
        if (!res.ok) throw await toError(res);
        return res.status === 204 ? null : res.json();
    }

    async function del(url, headers = {}) {
        const res = await fetch(url, {method: 'DELETE', headers});
        if (!res.ok) throw await toError(res);
        return null;
    }

    async function toError(res) {
        let message = '';
        try {
            const contentType = res.headers.get('content-type') || '';
            if (contentType.includes('application/json')) {
                const json = await res.json();
                message = json.message || JSON.stringify(json);
            } else {
                message = await res.text();
            }
        } catch (_) {
        }
        const err = new Error(message || ('HTTP ' + res.status));
        err.status = res.status;
        return err;
    }

    async function fetchAllPages(basePath, size = 100) {
        const all = [];
        let page = 0;
        while (true) {
            const data = await getJson(`${basePath}?page=${page}&size=${size}`);
            const items = Array.isArray(data) ? data : Object.values(data).find(Array.isArray) || [];
            all.push(...items);
            if (!data.hasNext) break;
            page++;
        }
        return all;
    }

    return {
        listThemes: async (page = 0, size = 10) => {
            const data = await getJson(`/api/themes?page=${page}&size=${size}`);
            return {items: data.themes || [], hasNext: data.hasNext ?? false};
        },
        listAllThemes: () => fetchAllPages('/api/themes'),
        popularThemes: async () => {
            const data = await getJson('/api/themes/popular');
            return data.themes || [];
        },
        listReservations: async (userName, page = 0, size = 10) => {
            if (userName) {
                const data = await getJson('/api/reservations?user_name=' + encodeURIComponent(userName));
                return {items: data.reservations || [], hasNext: false};
            }
            const data = await getJson(`/api/reservations?page=${page}&size=${size}`);
            return {items: data.reservations || [], hasNext: data.hasNext ?? false};
        },

        createReservation: (payload) => postJson('/api/reservations', payload),
        deleteReservation: (id, userName) => del('/api/reservations/' + id, {'User-Name': encodeURIComponent(userName)}),
        updateReservation: (id, payload, userName) => putJson('/api/reservations/' + id, payload, {'User-Name': encodeURIComponent(userName)}),
        deleteReservationByAdmin: (id) => del('/api/admin/reservations/' + id),

        listTimes: async () => {
            const data = await getJson('/api/times');
            return data.reservationTimes || [];
        },
        availableTimes: async (themeId, date) => {
            const data = await getJson('/api/times/available?theme_id=' + encodeURIComponent(themeId) + '&date=' + encodeURIComponent(date));
            return data.reservationTimes || [];
        },
        createTheme: (payload) => postJson('/api/admin/themes', payload),
        deleteTheme: (id) => del('/api/admin/themes/' + id),
        createTime: (payload) => postJson('/api/admin/times', payload),
        deleteTime: (id) => del('/api/admin/times/' + id)
    };
})();
