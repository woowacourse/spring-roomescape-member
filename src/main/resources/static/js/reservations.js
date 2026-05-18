(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', async function () {
        const params = new URLSearchParams(location.search);
        const user = params.get('user');

        const gateSection = document.getElementById('gate-section');
        const ledgerSection = document.getElementById('ledger-section');
        const ledgerBody = document.getElementById('ledger-body');
        const ledgerCount = document.getElementById('ledger-count');
        const ledgerCountMeta = document.getElementById('ledger-count-meta');
        const ledgerEmpty = document.getElementById('ledger-empty');
        const ledgerTable = document.getElementById('ledger-table');

        if (!user || !user.trim()) {
            gateSection.style.display = '';
            ledgerSection.style.display = 'none';
            return;
        }

        gateSection.style.display = 'none';
        ledgerSection.style.display = '';
        document.querySelectorAll('[data-bind="user"]').forEach(el => el.textContent = user);

        let themeCache = [];
        try {
            themeCache = await api.listThemes();
        } catch (e) {
            modal.alert({title: '테마 로드 실패', message: e.message});
        }

        try {
            const reservations = await api.listReservations(user);
            renderLedger(reservations);
        } catch (e) {
            modal.alert({title: '조회 실패', message: e.message});
        }

        function renderLedger(items) {
            updateCount(items.length);
            if (items.length === 0) {
                ledgerTable.style.display = 'none';
                ledgerEmpty.style.display = 'block';
                return;
            }
            ledgerEmpty.style.display = 'none';
            ledgerTable.style.display = '';
            ledgerBody.innerHTML = items.map(rowHtml).join('');
            wireRowActions();
        }

        function rowHtml(r) {
            return `
                <tr data-id="${r.id}"
                    data-theme-id="${r.theme && r.theme.id || ''}"
                    data-theme-name="${escapeAttr(r.theme && r.theme.name || '')}"
                    data-date="${escapeAttr(r.date || '')}"
                    data-time-id="${r.time && r.time.id || ''}"
                    data-time-start="${escapeAttr(r.time && r.time.startAt || '')}">
                    <td class="col-id">#${r.id}</td>
                    <td>${escapeHtml(r.theme && r.theme.name || '')}</td>
                    <td>${escapeHtml(r.date || '')}</td>
                    <td>${escapeHtml((r.time && r.time.startAt || '').slice(0, 5))}</td>
                    <td class="col-actions">
                        <button type="button" class="btn btn-ghost btn-sm" data-action="edit" data-id="${r.id}">변경</button>
                        <button type="button" class="btn btn-danger btn-sm" data-action="delete" data-id="${r.id}">폐기</button>
                    </td>
                </tr>
            `;
        }

        function wireRowActions() {
            ledgerBody.querySelectorAll('[data-action="delete"]').forEach(btn => {
                btn.addEventListener('click', () => onDelete(btn.dataset.id));
            });
            ledgerBody.querySelectorAll('[data-action="edit"]').forEach(btn => {
                btn.addEventListener('click', () => openEditDialog(btn.dataset.id));
            });
        }

        function updateCount(n) {
            ledgerCount.textContent = n;
            if (ledgerCountMeta) ledgerCountMeta.textContent = n;
        }

        async function onDelete(id) {
            const ok = await modal.confirm({
                title: '예약 폐기',
                message: '이 예약을 폐기하면 복구할 수 없습니다. 계속하시겠습니까?',
                okText: '폐기 확정',
                danger: true
            });
            if (!ok) return;
            try {
                await api.deleteReservation(id);
                removeRow(id);
            } catch (e) {
                modal.alert({title: '폐기 실패', message: e.message});
            }
        }

        function removeRow(id) {
            const row = ledgerBody.querySelector(`tr[data-id="${id}"]`);
            if (row) row.remove();
            const remaining = ledgerBody.querySelectorAll('tr').length;
            updateCount(remaining);
            if (remaining === 0) {
                ledgerTable.style.display = 'none';
                ledgerEmpty.style.display = 'block';
            }
        }

        // ── 예약 변경 (PATCH) ──────────────────────────────────────────────

        const editDialog = document.getElementById('edit-dialog');
        const editForm = document.getElementById('edit-form');
        const editTheme = document.getElementById('edit-theme');
        const editDate = document.getElementById('edit-date');
        const editSlots = document.getElementById('edit-slots');
        const editSlotsHelp = document.getElementById('edit-slots-help');
        const editRef = document.getElementById('edit-ref');
        const editOwnerLabel = document.getElementById('edit-owner-label');

        let editing = null; // { id, themeId, date, timeId, timeStart }

        editForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            if (!editing) return;
            const checked = editSlots.querySelector('input[name="timeId"]:checked');
            if (!checked) {
                await modal.alert({message: '시간을 선택하세요.'});
                return;
            }
            const payload = {
                userName: user,
                themeId: Number(editTheme.value),
                date: editDate.value,
                timeId: Number(checked.value)
            };
            try {
                const updated = await api.updateReservation(editing.id, payload);
                applyUpdate(updated);
                editDialog.close();
                editing = null;
            } catch (err) {
                await modal.alert({title: '변경 실패', message: err.message});
            }
        });

        editForm.querySelectorAll('[data-action="cancel"]').forEach(btn => {
            btn.addEventListener('click', () => {
                editDialog.close();
                editing = null;
            });
        });
        editDialog.addEventListener('cancel', (e) => {
            e.preventDefault();
            editDialog.close();
            editing = null;
        });
        editDialog.addEventListener('click', (e) => {
            if (e.target === editDialog) {
                editDialog.close();
                editing = null;
            }
        });

        editTheme.addEventListener('change', refreshSlots);
        editDate.addEventListener('change', refreshSlots);

        function openEditDialog(id) {
            const row = ledgerBody.querySelector(`tr[data-id="${id}"]`);
            if (!row) return;
            if (themeCache.length === 0) {
                modal.alert({message: '테마 정보를 불러오지 못했습니다.'});
                return;
            }
            editing = {
                id: id,
                themeId: row.dataset.themeId,
                date: row.dataset.date,
                timeId: row.dataset.timeId,
                timeStart: row.dataset.timeStart
            };
            editRef.textContent = '#' + id;
            editOwnerLabel.textContent = user;
            editTheme.innerHTML = themeCache.map(t =>
                `<option value="${t.id}" ${String(t.id) === String(editing.themeId) ? 'selected' : ''}>${escapeHtml(t.name)}</option>`
            ).join('');
            const today = new Date().toISOString().slice(0, 10);
            editDate.min = today;
            editDate.value = editing.date && editing.date >= today ? editing.date : today;
            refreshSlots();
            if (typeof editDialog.showModal === 'function') {
                editDialog.showModal();
            } else {
                modal.alert({message: '이 브라우저는 변경 다이얼로그를 지원하지 않습니다.'});
            }
        }

        async function refreshSlots() {
            const themeId = editTheme.value;
            const date = editDate.value;
            if (!themeId || !date) {
                editSlots.innerHTML = '';
                editSlotsHelp.textContent = '사건과 일자를 정하면 가용 시간이 표시됩니다';
                return;
            }
            editSlots.innerHTML = '<div class="slot-loading">불러오는 중…</div>';
            try {
                const available = await api.availableTimes(themeId, date);
                renderEditSlots(available);
            } catch (e) {
                editSlots.innerHTML = '';
                editSlotsHelp.textContent = '시간 조회 실패: ' + e.message;
            }
        }

        function renderEditSlots(slots) {
            // 자기 자신의 현재 슬롯은 항상 선택 가능해야 한다 (동일 (date/theme/time) 유지하는 경우)
            const sameDate = editDate.value === editing.date;
            const sameTheme = String(editTheme.value) === String(editing.themeId);
            const list = slots.slice();
            if (sameDate && sameTheme && editing.timeId
                && !list.some(s => String(s.id) === String(editing.timeId))) {
                list.push({id: Number(editing.timeId), startAt: editing.timeStart, _self: true});
                list.sort((a, b) => (a.startAt || '').localeCompare(b.startAt || ''));
            }
            if (list.length === 0) {
                editSlots.innerHTML = '';
                editSlotsHelp.textContent = '※ 이 사건/일자의 모든 시간이 마감되었습니다';
                return;
            }
            editSlotsHelp.textContent = '가용 ' + list.length + '건';
            editSlots.innerHTML = list.map((s) => {
                const checked = String(s.id) === String(editing.timeId) ? 'checked' : '';
                const mark = s._self ? ' · 현재' : '';
                return `
                    <div class="slot">
                        <input type="radio" id="edit-slot-${s.id}" name="timeId" value="${s.id}" ${checked}>
                        <label for="edit-slot-${s.id}">${(s.startAt || '').slice(0, 5)}${mark}</label>
                    </div>
                `;
            }).join('');
            // 아무것도 체크 안 됐으면 첫 번째 선택
            if (!editSlots.querySelector('input[name="timeId"]:checked')) {
                const first = editSlots.querySelector('input[name="timeId"]');
                if (first) first.checked = true;
            }
        }

        function applyUpdate(updated) {
            const row = ledgerBody.querySelector(`tr[data-id="${updated.id}"]`);
            if (!row) return;
            row.dataset.themeId = updated.theme && updated.theme.id || '';
            row.dataset.themeName = updated.theme && updated.theme.name || '';
            row.dataset.date = updated.date || '';
            row.dataset.timeId = updated.time && updated.time.id || '';
            row.dataset.timeStart = updated.time && updated.time.startAt || '';
            const tds = row.querySelectorAll('td');
            tds[1].textContent = updated.theme && updated.theme.name || '';
            tds[2].textContent = updated.date || '';
            tds[3].textContent = (updated.time && updated.time.startAt || '').slice(0, 5);
        }
    });

    function escapeHtml(s) {
        return String(s).replace(/[&<>"']/g, c => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[c]));
    }

    function escapeAttr(s) {
        return escapeHtml(s);
    }
})();
