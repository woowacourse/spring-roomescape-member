/* Modal dialog API: showConfirm / showAlert — Promise-based.
   Replaces native window.confirm / window.alert. */
window.modal = (function () {
    'use strict';

    let dialog, titleEl, messageEl, okBtn, cancelBtn, closeBtn, footer;

    function ensureMounted() {
        dialog = document.getElementById('confirm-dialog');
        if (!dialog) return false;
        titleEl = dialog.querySelector('[data-role="title"]');
        messageEl = dialog.querySelector('[data-role="message"]');
        okBtn = dialog.querySelector('[data-action="confirm"]');
        cancelBtn = dialog.querySelector('[data-action="cancel"]');
        closeBtn = dialog.querySelector('.dialog-close');
        footer = dialog.querySelector('.dialog-foot');
        return true;
    }

    function open(opts, isAlert) {
        return new Promise(function (resolve) {
            if (!ensureMounted() || typeof dialog.showModal !== 'function') {
                if (isAlert) {
                    window.alert(opts.message || '');
                    resolve(true);
                } else {
                    resolve(window.confirm(opts.message || ''));
                }
                return;
            }

            titleEl.textContent = opts.title || (isAlert ? '안내' : '확인이 필요합니다');
            messageEl.textContent = opts.message || '';
            okBtn.textContent = opts.okText || (isAlert ? '확인' : '확정');

            cancelBtn.style.display = isAlert ? 'none' : '';
            okBtn.classList.toggle('btn-primary', true);
            okBtn.classList.toggle('btn-danger', !!opts.danger && !isAlert);

            const cleanup = (value) => {
                okBtn.removeEventListener('click', onOk);
                cancelBtn.removeEventListener('click', onCancel);
                closeBtn.removeEventListener('click', onCancel);
                dialog.removeEventListener('cancel', onDialogCancel);
                dialog.removeEventListener('click', onBackdrop);
                dialog.close();
                resolve(value);
            };

            const onOk = () => cleanup(true);
            const onCancel = () => cleanup(isAlert ? true : false);
            const onDialogCancel = (e) => {
                e.preventDefault();
                cleanup(isAlert ? true : false);
            };
            const onBackdrop = (e) => {
                if (e.target === dialog) cleanup(isAlert ? true : false);
            };

            okBtn.addEventListener('click', onOk);
            cancelBtn.addEventListener('click', onCancel);
            closeBtn.addEventListener('click', onCancel);
            dialog.addEventListener('cancel', onDialogCancel);
            dialog.addEventListener('click', onBackdrop);

            dialog.showModal();
        });
    }

    return {
        confirm: (opts) => open(opts || {}, false),
        alert: (opts) => open(typeof opts === 'string' ? {message: opts} : (opts || {}), true)
    };
})();
