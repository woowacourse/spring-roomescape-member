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

    async function del(url) {
        const res = await fetch(url, {method: 'DELETE'});
        if (!res.ok) throw await toError(res);
        return null;
    }

    async function toError(res) {
        let message = '';
        try {
            message = await res.text();
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
            const items = await getJson(`${basePath}?page=${page}&size=${size}`);
            all.push(...items);
            if (items.length < size) break;
            page++;
        }
        return all;
    }

    return {
        listThemes: (page = 0, size = 10) =>
            getJson(`/api/themes?page=${page}&size=${size}`),
        listAllThemes: () => fetchAllPages('/api/themes'),
        popularThemes: () => getJson('/api/themes/popular'),
        listReservations: (userName, page = 0, size = 10) =>
            getJson(userName
                ? '/api/reservations?user_name=' + encodeURIComponent(userName)
                : `/api/reservations?page=${page}&size=${size}`),

        createReservation: (payload) => postJson('/api/reservations', payload),
        deleteReservation: (id) => del('/api/reservations/' + id),
        listTimes: () => getJson('/api/times'),
        availableTimes: (themeId, date) =>
            getJson('/api/times/available?theme_id=' + encodeURIComponent(themeId) + '&date=' + encodeURIComponent(date)),
        createTheme: (payload) => postJson('/api/admin/themes', payload),
        deleteTheme: (id) => del('/api/admin/themes/' + id),
        createTime: (payload) => postJson('/api/admin/times', payload),
        deleteTime: (id) => del('/api/admin/times/' + id)
    };
})();
