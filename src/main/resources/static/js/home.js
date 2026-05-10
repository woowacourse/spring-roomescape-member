(function () {
    'use strict';

    const PAGE_SIZE = 10;
    let themePage = 0;
    let hasMoreThemes = false;

    document.addEventListener('DOMContentLoaded', async function () {
        try {
            const [themes, popular] = await Promise.all([
                api.listThemes(0, PAGE_SIZE),
                api.popularThemes()
            ]);
            hasMoreThemes = themes.length === PAGE_SIZE;

            const popularStrip = document.getElementById('popular-strip');
            const popularEmpty = document.getElementById('popular-empty');
            const popularCount = document.getElementById('popular-count');
            renderStrip(popular, popularStrip, popularEmpty, popularCount);

            renderThemesPage(themes);
        } catch (e) {
            modal.alert({title: '데이터 로드 실패', message: e.message || '서버에 연결할 수 없습니다.'});
        }
    });

    function renderThemesPage(items) {
        const themesGrid = document.getElementById('themes-grid');
        const themesEmpty = document.getElementById('themes-empty');
        const themesCount = document.getElementById('themes-count');
        const statThemes = document.getElementById('stat-themes');

        renderGrid(items, themesGrid, themesEmpty, themesCount, statThemes);
        renderPagination();
    }

    function renderPagination() {
        let paginationEl = document.getElementById('themes-pagination');
        if (!paginationEl) {
            paginationEl = document.createElement('div');
            paginationEl.id = 'themes-pagination';
            paginationEl.style.cssText =
                'display:flex; align-items:center; gap:0.8rem; margin:1.5rem 0 2rem; justify-content:flex-end;';
            document.getElementById('themes-grid').insertAdjacentElement('afterend', paginationEl);
        }
        paginationEl.innerHTML = `
            <button class="btn btn-ghost btn-sm" id="themes-prev"
                    style="${themePage === 0 ? 'opacity:0.3;pointer-events:none;' : ''}">← 이전</button>
            <span style="font-family:var(--font-mono);font-size:0.75rem;letter-spacing:0.15em;">
                ${themePage + 1} 페이지
            </span>
            <button class="btn btn-ghost btn-sm" id="themes-next"
                    style="${!hasMoreThemes ? 'opacity:0.3;pointer-events:none;' : ''}">다음 →</button>
        `;
        if (themePage > 0) {
            document.getElementById('themes-prev').addEventListener('click', () => loadThemePage(-1));
        }
        if (hasMoreThemes) {
            document.getElementById('themes-next').addEventListener('click', () => loadThemePage(1));
        }
    }

    async function loadThemePage(delta) {
        themePage += delta;
        try {
            const themes = await api.listThemes(themePage, PAGE_SIZE);
            hasMoreThemes = themes.length === PAGE_SIZE;
            renderThemesPage(themes);
            document.getElementById('themes').scrollIntoView({behavior: 'smooth'});
        } catch (e) {
            themePage -= delta; // rollback
            modal.alert({title: '로드 실패', message: e.message});
        }
    }

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
        emptyEl.style.display = 'none';
        gridEl.style.display = '';
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
        return String(s).replace(/[&<>"']/g, c => (
            {'&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'}[c]
        ));
    }

    function escapeAttr(s) { return escapeHtml(s); }
})();
