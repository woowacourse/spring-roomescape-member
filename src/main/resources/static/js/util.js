(() => {
    // date(YYYY-MM-DD)가 오늘이고 startAt(HH:mm)이 현재 시각 이전이면 true.
    function isPastSlot(date, startAt) {
        const now = new Date();
        const todayStr = new Date(now.getTime() - now.getTimezoneOffset() * 60000)
            .toISOString().slice(0, 10);
        if (date !== todayStr) return false;

        const [hour, minute] = startAt.split(':').map(Number);
        if (hour !== now.getHours()) return hour < now.getHours();
        return minute <= now.getMinutes();
    }

    window.isPastSlot = isPastSlot;
})();
