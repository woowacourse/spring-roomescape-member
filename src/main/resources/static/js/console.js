(function () {
    'use strict';

    const PAGE_SIZE = 10;
    let resPage = 0;
    let hasMoreRes = false;
    let themePage = 0;
    let hasMoreThemes = false;

    document.addEventListener('DOMContentLoaded', async function () {
        await reloadAll();
        document.getElementById('theme-form').addEventListener('submit', onCreateTheme);
        document.getElementById('time-form').addEventListener('submit', onCreateTime);
    });

    async function reloadAll() {
        try {
            const [reservations, themes, times] = await Promise.all([
                api.listReservations(null, resPage, PAGE_SIZE),
                api.listThemes(themePage, PAGE_SIZE),
                api.listTimes()
            ]);
            hasMoreRes = reservations.length === PAGE_SIZE;
            hasMoreThemes = themes.length === PAGE_SIZE;
            renderReservations(reservations);
            renderThemes(themes);
            renderTimes(times);
        } catch (e) {
            modal.alert({title: '데이터 로드 실패', message: e.message});
        }
    }

    /* ── 예약 ── */
    function renderReservations(items) {
        const body = document.getElementById('res-body');
        const count = document.getElementById('res-count');
        const empty = document.getElementById('res-empty');
        const wrap = document.getElementById('res-wrap');
        count.textContent = items.length;
        if (items.length === 0 && resPage === 0) {
            wrap.style.display = 'none';
            empty.style.display = 'block';
        } else {
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
                        <button type="button" class="btn btn-danger btn-sm"
                                data-kind="reservation" data-id="${r.id}">폐기</button>
                    </td>
                </tr>
            `).join('');
            wireDeletes(body, 'reservation');
        }
        renderResPagination();
    }

    function renderResPagination() {
        let el = document.getElementById('res-pagination');
        if (!el) {
            el = document.createElement('div');
            el.id = 'res-pagination';
            el.style.cssText =
                'display:flex; align-items:center; gap:0.8rem; margin:1rem 0 2rem; justify-content:flex-end;';
            document.getElementById('res-wrap').insertAdjacentElement('afterend', el);
        }
        el.innerHTML = `
            <button class="btn btn-ghost btn-sm" id="res-prev"
                    style="${resPage === 0 ? 'opacity:0.3;pointer-events:none;' : ''}">← 이전</button>
            <span style="font-family:var(--font-mono);font-size:0.75rem;letter-spacing:0.15em;">
                ${resPage + 1} 페이지
            </span>
            <button class="btn btn-ghost btn-sm" id="res-next"
                    style="${!hasMoreRes ? 'opacity:0.3;pointer-events:none;' : ''}">다음 →</button>
        `;
        if (resPage > 0) {
            document.getElementById('res-prev').addEventListener('click', async () => {
                resPage--;
                const items = await api.listReservations(null, resPage, PAGE_SIZE);
                hasMoreRes = items.length === PAGE_SIZE;
                renderReservations(items);
            });
        }
        if (hasMoreRes) {
            document.getElementById('res-next').addEventListener('click', async () => {
                resPage++;
                const items = await api.listReservations(null, resPage, PAGE_SIZE);
                hasMoreRes = items.length === PAGE_SIZE;
                renderReservations(items);
            });
        }
    }

    function renderThemes(items) {
        const body = document.getElementById('theme-body');
        const count = document.getElementById('theme-count');
        const empty = document.getElementById('theme-empty');
        const wrap = document.getElementById('theme-wrap');
        count.textContent = items.length;
        document.getElementById('theme-count-section').textContent = items.length;
        if (items.length === 0 && themePage === 0) {
            wrap.style.display = 'none';
            empty.style.display = 'block';
        } else {
            empty.style.display = 'none';
            wrap.style.display = '';
            body.innerHTML = items.map(t => `
                <tr data-id="${t.id}">
                    <td class="col-id">#${t.id}</td>
                    <td>${escapeHtml(t.name || '')}</td>
                    <td class="col-actions">
                        <button type="button" class="btn btn-danger btn-sm"
                                data-kind="theme" data-id="${t.id}">폐기</button>
                    </td>
                </tr>
            `).join('');
            wireDeletes(body, 'theme');
        }
        renderThemePagination();
    }

    function renderThemePagination() {
        let el = document.getElementById('theme-pagination');
        if (!el) {
            el = document.createElement('div');
            el.id = 'theme-pagination';
            el.style.cssText =
                'display:flex; align-items:center; gap:0.8rem; margin:0.8rem 0; justify-content:flex-end;';
            document.getElementById('theme-wrap').insertAdjacentElement('afterend', el);
        }
        el.innerHTML = `
            <button class="btn btn-ghost btn-sm" id="theme-prev"
                    style="${themePage === 0 ? 'opacity:0.3;pointer-events:none;' : ''}">← 이전</button>
            <span style="font-family:var(--font-mono);font-size:0.75rem;letter-spacing:0.15em;">
                ${themePage + 1} 페이지
            </span>
            <button class="btn btn-ghost btn-sm" id="theme-next"
                    style="${!hasMoreThemes ? 'opacity:0.3;pointer-events:none;' : ''}">다음 →</button>
        `;
        if (themePage > 0) {
            document.getElementById('theme-prev').addEventListener('click', async () => {
                themePage--;
                const items = await api.listThemes(themePage, PAGE_SIZE);
                hasMoreThemes = items.length === PAGE_SIZE;
                renderThemes(items);
            });
        }
        if (hasMoreThemes) {
            document.getElementById('theme-next').addEventListener('click', async () => {
                themePage++;
                const items = await api.listThemes(themePage, PAGE_SIZE);
                hasMoreThemes = items.length === PAGE_SIZE;
                renderThemes(items);
            });
        }
    }

    function renderTimes(items) {
        const body = document.getElementById('time-body');
        const count = document.getElementById('time-count');
        const empty = document.getElementById('time-empty');
        const wrap = document.getElementById('time-wrap');
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
                    <button type="button" class="btn btn-danger btn-sm"
                            data-kind="time" data-id="${s.id}">폐기</button>
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
            reservation: {title: '예약 폐기', msg: '이 예약을 폐기하면 복구할 수 없습니다.', call: api.deleteReservation},
            theme: {title: '사건 폐기', msg: '이 사건(테마)을 폐기하면 복구할 수 없습니다.', call: api.deleteTheme},
            time: {title: '시간 슬롯 폐기', msg: '이 시간 슬롯을 폐기합니다. 진행 중인 예약이 있으면 차단됩니다.', call: api.deleteTime}
        };
        const m = map[kind];
        const ok = await modal.confirm({title: m.title, message: m.msg, okText: '폐기 확정', danger: true});
        if (!ok) return;
        try {
            await m.call(id);
            if (kind === 'reservation') {
                const items = await api.listReservations(null, resPage, PAGE_SIZE);
                if (items.length === 0 && resPage > 0) resPage--;
                const refetched = await api.listReservations(null, resPage, PAGE_SIZE);
                hasMoreRes = refetched.length === PAGE_SIZE;
                renderReservations(refetched);
            } else if (kind === 'theme') {
                const items = await api.listThemes(themePage, PAGE_SIZE);
                if (items.length === 0 && themePage > 0) themePage--;
                const refetched = await api.listThemes(themePage, PAGE_SIZE);
                hasMoreThemes = refetched.length === PAGE_SIZE;
                renderThemes(refetched);
            } else {
                const times = await api.listTimes();
                renderTimes(times);
            }
        } catch (e) {
            modal.alert({title: '삭제 실패', message: e.message});
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
            await modal.alert({message: '모든 항목을 입력하세요.'});
            return;
        }
        try {
            await api.createTheme(payload);
            e.target.reset();
            themePage = 0; // 등록 후 첫 페이지로
            const items = await api.listThemes(0, PAGE_SIZE);
            hasMoreThemes = items.length === PAGE_SIZE;
            renderThemes(items);
        } catch (err) {
            modal.alert({title: '등록 실패', message: err.message});
        }
    }

    async function onCreateTime(e) {
        e.preventDefault();
        const fd = new FormData(e.target);
        const startAt = (fd.get('startAt') || '').trim();
        if (!startAt) {
            await modal.alert({message: '시각을 입력하세요.'});
            return;
        }
        try {
            await api.createTime({startAt});
            e.target.reset();
            const times = await api.listTimes();
            renderTimes(times);
        } catch (err) {
            modal.alert({title: '등록 실패', message: err.message});
        }
    }

    function escapeHtml(s) {
        return String(s).replace(/[&<>"']/g, c => (
            {'&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'}[c]
        ));
    }
})();
