(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', async function () {
        const params = new URLSearchParams(location.search);
        const themeId = params.get('themeId');
        if (!themeId) {
            modal.alert({title: '오류', message: 'themeId가 필요합니다.'});
            location.href = '/';
            return;
        }

        const today = new Date().toISOString().slice(0, 10);
        const dateInput = document.getElementById('date');
        const dateForm = document.getElementById('date-form');
        const slotsArea = document.getElementById('slots-area');
        const slotsList = document.getElementById('slots-list');
        const slotsCount = document.getElementById('slots-count');
        const slotsDate = document.getElementById('slots-date');
        const slotsEmpty = document.getElementById('slots-empty');
        const reserveForm = document.getElementById('reserve-form');
        const todayLabel = document.getElementById('today-label');

        if (todayLabel) todayLabel.textContent = today;
        if (dateInput) {
            dateInput.min = today;
            if (!dateInput.value) dateInput.value = today;
        }

        try {
            const themes = await api.listAllThemes();
            const theme = themes.find(t => String(t.id) === String(themeId));
            if (!theme) {
                modal.alert({title: '없는 사건', message: '해당 사건을 찾을 수 없습니다.'});
                location.href = '/';
                return;
            }
            renderBanner(theme);
        } catch (e) {
            modal.alert({title: '로드 실패', message: e.message});
            return;
        }

        dateForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const date = dateInput.value;
            if (!date) return;
            if (date < today) {
                await modal.alert({title: '잘못된 일자', message: '오늘 이후의 날짜만 선택할 수 있습니다.'});
                return;
            }
            await loadSlots(themeId, date);
        });

        reserveForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const date = dateInput.value;
            if (date < today) {
                await modal.alert({title: '잘못된 일자', message: '오늘 이후의 날짜만 선택할 수 있습니다.'});
                return;
            }
            const fd = new FormData(reserveForm);
            const payload = {
                userName: (fd.get('userName') || '').trim(),
                themeId: Number(themeId),
                date: date,
                timeId: Number(fd.get('timeId'))
            };
            if (!payload.userName) {
                await modal.alert({message: '탐정 이름을 입력하세요.'});
                return;
            }
            if (!payload.timeId) {
                await modal.alert({message: '시간을 선택하세요.'});
                return;
            }
            try {
                await api.createReservation(payload);
                location.href = '/reservations?user=' + encodeURIComponent(payload.userName);
            } catch (err) {
                await modal.alert({title: '예약 실패', message: err.message || '서버 오류가 발생했습니다.'});
            }
        });

        async function loadSlots(themeId, date) {
            try {
                const slots = await api.availableTimes(themeId, date);
                slotsArea.style.display = 'block';
                slotsDate.textContent = date;
                slotsCount.textContent = slots.length;
                if (slots.length === 0) {
                    slotsEmpty.style.display = 'block';
                    reserveForm.style.display = 'none';
                    return;
                }
                slotsEmpty.style.display = 'none';
                reserveForm.style.display = '';
                slotsList.innerHTML = slots.map((s, i) => `
                    <div class="slot">
                        <input type="radio" id="slot-${s.id}" name="timeId" value="${s.id}" ${i === 0 ? 'checked' : ''} required>
                        <label for="slot-${s.id}">${(s.startAt || '').slice(0, 5)}</label>
                    </div>
                `).join('');
            } catch (e) {
                modal.alert({title: '시간 조회 실패', message: e.message});
            }
        }
    });

    function renderBanner(theme) {
        const banner = document.getElementById('case-banner');
        const initial = (theme.name || '?').charAt(0);
        banner.innerHTML = `
            <div class="thumb" data-initial="${escapeAttr(initial)}">
                <img src="${escapeAttr(theme.thumbnail || '')}" alt="${escapeAttr(theme.name || '')}"
                     loading="lazy" referrerpolicy="no-referrer"
                     onerror="this.remove();">
            </div>
            <div>
                <span class="filenum">CASE FILE · No. ${theme.id} / 2026</span>
                <h1>${escapeHtml(theme.name || '')}</h1>
                <p>${escapeHtml(theme.description || '')}</p>
            </div>
        `;
    }

    function escapeHtml(s) {
        return String(s).replace(/[&<>"']/g, c => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[c]));
    }

    function escapeAttr(s) {
        return escapeHtml(s);
    }
})();
