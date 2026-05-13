/* Thin fetch wrappers around the learner's JSON REST API. */
window.api = (function () {
    'use strict';

    async function getJson(url) {
        const res = await fetch(url, {headers: {'Accept': 'application/json'}});
        if (!res.ok) throw await toError(res);
        return res.json();
    }

    async function postJson(url, body) {
        return sendJson('POST', url, body);
    }

    async function patchJson(url, body) {
        return sendJson('PATCH', url, body);
    }

    async function sendJson(method, url, body) {
        const res = await fetch(url, {
            method: method,
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
        const ct = res.headers.get('content-type') || '';
        let message = '';
        try {
            if (ct.includes('application/problem+json') || ct.includes('application/json')) {
                const body = await res.json();
                message = body.detail || body.title || body.message || '';
            } else {
                message = await res.text();
            }
        } catch (_) { /* ignore parse errors */
        }
        const err = new Error(message || ('HTTP ' + res.status));
        err.status = res.status;
        return err;
    }

    return {
        listThemes: () => getJson('/api/themes'),
        popularThemes: () => getJson('/api/themes/popularity'),
        listReservations: (userName) => getJson(userName ? '/api/reservations?user_name=' + encodeURIComponent(userName) : '/api/reservations'),
        createReservation: (payload) => postJson('/api/reservations', payload),
        updateReservation: (id, payload) => patchJson('/api/reservations/' + id, payload),
        deleteReservation: (id) => del('/api/reservations/' + id),
        listTimes: () => getJson('/api/times'),
        availableTimes: (themeId, date) => getJson('/api/times/availability?theme_id=' + encodeURIComponent(themeId) + '&date=' + encodeURIComponent(date)),
        createTheme: (payload) => postJson('/admin/themes', payload),
        deleteTheme: (id) => del('/admin/themes/' + id),
        createTime: (payload) => postJson('/admin/times', payload),
        deleteTime: (id) => del('/admin/times/' + id)
    };
})();
