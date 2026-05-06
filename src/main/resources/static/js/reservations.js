(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', async function () {
        const params = new URLSearchParams(location.search);
        const user = params.get('user');

        const gateSection = document.getElementById('gate-section');
        const ledgerSection = document.getElementById('ledger-section');
        const userLabel = document.getElementById('user-label');
        const ledgerBody = document.getElementById('ledger-body');
        const ledgerCount = document.getElementById('ledger-count');
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

        try {
            const reservations = await api.listReservations(user);
            renderLedger(reservations);
        } catch (e) {
            modal.alert({ title: '조회 실패', message: e.message });
        }

        function renderLedger(items) {
            ledgerCount.textContent = items.length;
            if (items.length === 0) {
                ledgerTable.style.display = 'none';
                ledgerEmpty.style.display = 'block';
                return;
            }
            ledgerEmpty.style.display = 'none';
            ledgerTable.style.display = '';
            ledgerBody.innerHTML = items.map(r => `
                <tr data-id="${r.id}">
                    <td class="col-id">#${r.id}</td>
                    <td>${escapeHtml(r.theme && r.theme.name || '')}</td>
                    <td>${escapeHtml(r.date || '')}</td>
                    <td>${escapeHtml((r.time && r.time.startAt || '').slice(0, 5))}</td>
                    <td class="col-actions">
                        <button type="button" class="btn btn-danger btn-sm" data-action="delete" data-id="${r.id}">폐기</button>
                    </td>
                </tr>
            `).join('');
            ledgerBody.querySelectorAll('[data-action="delete"]').forEach(btn => {
                btn.addEventListener('click', () => onDelete(btn.dataset.id));
            });
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
                const row = ledgerBody.querySelector(`tr[data-id="${id}"]`);
                if (row) row.remove();
                const remaining = ledgerBody.querySelectorAll('tr').length;
                ledgerCount.textContent = remaining;
                if (remaining === 0) {
                    ledgerTable.style.display = 'none';
                    ledgerEmpty.style.display = 'block';
                }
            } catch (e) {
                modal.alert({ title: '삭제 실패', message: e.message });
            }
        }
    });

    function escapeHtml(s) {
        return String(s).replace(/[&<>"']/g, c => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[c]));
    }
})();
