(() => {
    const container = document.getElementById('toast-container');
    const DURATION_MS = 3500;

    function showToast(message, type) {
        if (!container) return;

        const toast = document.createElement('div');
        toast.className = 'toast ' + (type || 'error');

        const msg = document.createElement('p');
        msg.className = 'toast-message';
        msg.textContent = message;
        toast.appendChild(msg);

        container.appendChild(toast);
        requestAnimationFrame(() => toast.classList.add('show'));

        setTimeout(() => {
            toast.classList.remove('show');
            toast.addEventListener('transitionend', () => toast.remove(), {once: true});
        }, DURATION_MS);
    }

    window.showToast = showToast;
    window.toastError = (error) => showToast(error.message || '요청 처리에 실패했습니다.', 'error');
    window.toastSuccess = (message) => showToast(message, 'success');
})();
