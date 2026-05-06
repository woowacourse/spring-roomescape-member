(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', async function () {
        const popularStrip = document.getElementById('popular-strip');
        const popularEmpty = document.getElementById('popular-empty');
        const themesGrid   = document.getElementById('themes-grid');
        const themesEmpty  = document.getElementById('themes-empty');
        const themesCount  = document.getElementById('themes-count');
        const popularCount = document.getElementById('popular-count');
        const statThemes   = document.getElementById('stat-themes');

        try {
            const [themes, popular] = await Promise.all([
                api.listThemes(),
                api.popularThemes()
            ]);
            renderStrip(popular, popularStrip, popularEmpty, popularCount);
            renderGrid(themes, themesGrid, themesEmpty, themesCount, statThemes);
        } catch (e) {
            modal.alert({ title: '데이터 로드 실패', message: e.message || '서버에 연결할 수 없습니다.' });
        }
    });

    function renderStrip(items, stripEl, emptyEl, countEl) {
        countEl.textContent = items.length;
        if (items.length === 0) {
            stripEl.parentElement.style.display = 'none';
            emptyEl.style.display = 'block';
            return;
        }
        stripEl.innerHTML = items.map(themeCardHtml).join('');
    }

    function renderGrid(items, gridEl, emptyEl, countEl, statEl) {
        countEl.textContent = items.length;
        if (statEl) statEl.textContent = items.length;
        if (items.length === 0) {
            gridEl.style.display = 'none';
            emptyEl.style.display = 'block';
            return;
        }
        gridEl.innerHTML = items.map(themeCardHtml).join('');
    }

    function themeCardHtml(theme) {
        const initial = (theme.name || '?').charAt(0);
        const safeName = escapeHtml(theme.name || '');
        const safeDesc = escapeHtml(theme.description || '');
        const safeThumb = escapeAttr(theme.thumbnail || '');
        return `
        <a class="file-card" href="/booking?themeId=${theme.id}">
            <div class="thumb" data-initial="${escapeAttr(initial)}">
                <img src="${safeThumb}" alt="${escapeAttr(theme.name || '')}"
                     loading="lazy" referrerpolicy="no-referrer"
                     onerror="this.remove();">
            </div>
            <div class="body">
                <div class="row">
                    <span>FILE · ${theme.id}</span>
                    <span>OPEN</span>
                </div>
                <h3>${safeName}</h3>
                <p>${safeDesc}</p>
                <span class="cta">조사 시작 →</span>
            </div>
        </a>`;
    }

    function escapeHtml(s) {
        return String(s).replace(/[&<>"']/g, c => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[c]));
    }
    function escapeAttr(s) { return escapeHtml(s); }
})();
