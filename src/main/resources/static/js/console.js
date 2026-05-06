(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', async function () {
        await reloadAll();

        document.getElementById('theme-form').addEventListener('submit', onCreateTheme);
        document.getElementById('time-form').addEventListener('submit', onCreateTime);
    });

    async function reloadAll() {
        try {
            const [reservations, themes, times] = await Promise.all([
                api.listReservations(null),
                api.listThemes(),
                api.listTimes()
            ]);
            renderReservations(reservations);
            renderThemes(themes);
            renderTimes(times);
        } catch (e) {
            modal.alert({ title: '데이터 로드 실패', message: e.message });
        }
    }

    function renderReservations(items) {
        const body  = document.getElementById('res-body');
        const count = document.getElementById('res-count');
        const empty = document.getElementById('res-empty');
        const wrap  = document.getElementById('res-wrap');
        count.textContent = items.length;
        if (items.length === 0) {
            wrap.style.display = 'none';
            empty.style.display = 'block';
            return;
        }
        empty.style.display = 'none';
        wrap.style.display = '';
        body.innerHTML = items.map(r => `
            <tr data-id="${r.id}">
                <td class="col-id">#${r.id}</td>
                <td>${escapeHtml(r.userName || '')}</td>
                <td>${escapeHtml(r.theme && r.theme.name || '')}</td>
                <td>${escapeHtml(r.date || '')}</td>
                <td>${escapeHtml((r.time && r.time.startAt || '').slice(0, 5))}</td>
                <td class="col-actions">
                    <button type="button" class="btn btn-danger btn-sm" data-kind="reservation" data-id="${r.id}">폐기</button>
                </td>
            </tr>
        `).join('');
        wireDeletes(body, 'reservation');
    }

    function renderThemes(items) {
        const body  = document.getElementById('theme-body');
        const count = document.getElementById('theme-count');
        const empty = document.getElementById('theme-empty');
        const wrap  = document.getElementById('theme-wrap');
        count.textContent = items.length;
        document.getElementById('theme-count-section').textContent = items.length;
        if (items.length === 0) {
            wrap.style.display = 'none';
            empty.style.display = 'block';
            return;
        }
        empty.style.display = 'none';
        wrap.style.display = '';
        body.innerHTML = items.map(t => `
            <tr data-id="${t.id}">
                <td class="col-id">#${t.id}</td>
                <td>${escapeHtml(t.name || '')}</td>
                <td class="col-actions">
                    <button type="button" class="btn btn-danger btn-sm" data-kind="theme" data-id="${t.id}">폐기</button>
                </td>
            </tr>
        `).join('');
        wireDeletes(body, 'theme');
    }

    function renderTimes(items) {
        const body  = document.getElementById('time-body');
        const count = document.getElementById('time-count');
        const empty = document.getElementById('time-empty');
        const wrap  = document.getElementById('time-wrap');
        count.textContent = items.length;
        document.getElementById('time-count-section').textContent = items.length;
        if (items.length === 0) {
            wrap.style.display = 'none';
            empty.style.display = 'block';
            return;
        }
        empty.style.display = 'none';
        wrap.style.display = '';
        body.innerHTML = items.map(s => `
            <tr data-id="${s.id}">
                <td class="col-id">#${s.id}</td>
                <td>${escapeHtml((s.startAt || '').slice(0, 5))}</td>
                <td class="col-actions">
                    <button type="button" class="btn btn-danger btn-sm" data-kind="time" data-id="${s.id}">폐기</button>
                </td>
            </tr>
        `).join('');
        wireDeletes(body, 'time');
    }

    function wireDeletes(scope, kind) {
        scope.querySelectorAll(`[data-kind="${kind}"]`).forEach(btn => {
            btn.addEventListener('click', () => onDelete(kind, btn.dataset.id));
        });
    }

    async function onDelete(kind, id) {
        const map = {
            reservation: { title: '예약 폐기',  msg: '이 예약을 폐기하면 복구할 수 없습니다.',                       call: api.deleteReservation },
            theme:       { title: '사건 폐기',  msg: '이 사건(테마)을 폐기하면 복구할 수 없습니다.',                  call: api.deleteTheme },
            time:        { title: '시간 슬롯 폐기', msg: '이 시간 슬롯을 폐기합니다. 진행 중인 예약이 있으면 차단됩니다.', call: api.deleteTime }
        };
        const m = map[kind];
        const ok = await modal.confirm({ title: m.title, message: m.msg, okText: '폐기 확정', danger: true });
        if (!ok) return;
        try {
            await m.call(id);
            await reloadAll();
        } catch (e) {
            modal.alert({ title: '삭제 실패', message: e.message });
        }
    }

    async function onCreateTheme(e) {
        e.preventDefault();
        const fd = new FormData(e.target);
        const payload = {
            name: (fd.get('name') || '').trim(),
            description: (fd.get('description') || '').trim(),
            thumbnail: (fd.get('thumbnail') || '').trim()
        };
        if (!payload.name || !payload.description || !payload.thumbnail) {
            await modal.alert({ message: '모든 항목을 입력하세요.' });
            return;
        }
        try {
            await api.createTheme(payload);
            e.target.reset();
            await reloadAll();
        } catch (err) {
            modal.alert({ title: '등록 실패', message: err.message });
        }
    }

    async function onCreateTime(e) {
        e.preventDefault();
        const fd = new FormData(e.target);
        const startAt = (fd.get('startAt') || '').trim();
        if (!startAt) {
            await modal.alert({ message: '시각을 입력하세요.' });
            return;
        }
        try {
            await api.createTime({ startAt: startAt });
            e.target.reset();
            await reloadAll();
        } catch (err) {
            modal.alert({ title: '등록 실패', message: err.message });
        }
    }

    function escapeHtml(s) {
        return String(s).replace(/[&<>"']/g, c => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[c]));
    }
})();
