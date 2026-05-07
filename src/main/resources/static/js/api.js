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
        } catch (_) { /* ignore */
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
        deleteReservation: (id) => del('/api/reservations/' + id),
        listTimes: () => getJson('/api/times'),
        availableTimes: (themeId, date) => getJson('/api/times/availability?theme_id=' + encodeURIComponent(themeId) + '&date=' + encodeURIComponent(date)),
        createTheme: (payload) => postJson('/admin/themes', payload),
        deleteTheme: (id) => del('/admin/themes/' + id),
        createTime: (payload) => postJson('/admin/times', payload),
        deleteTime: (id) => del('/admin/times/' + id)
    };
})();
