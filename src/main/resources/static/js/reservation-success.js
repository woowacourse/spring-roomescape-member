(() => {
    const raw = sessionStorage.getItem('lastReservation');
    const successCard = document.getElementById('success-card');
    const emptyCard = document.getElementById('empty-card');

    if (!raw) {
        emptyCard.hidden = false;
        return;
    }
    try {
        const r = JSON.parse(raw);
        document.getElementById('r-id').textContent = r.id;
        document.getElementById('r-name').textContent = r.name;
        document.getElementById('r-theme').textContent = r.theme.name;
        document.getElementById('r-date').textContent = r.date;
        document.getElementById('r-time').textContent = r.time.startAt;
        document.getElementById('r-mine-link').href = `/reservation-mine?name=${encodeURIComponent(r.name)}`;
        successCard.hidden = false;
    } catch (e) {
        emptyCard.hidden = false;
    }
})();
