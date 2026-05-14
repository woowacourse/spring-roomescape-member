document.addEventListener('DOMContentLoaded', function () {
    var btn = document.getElementById('_errModalBtn');
    if (btn) {
        btn.addEventListener('click', function () {
            document.getElementById('_errModal').style.display = 'none';
        });
    }
});

function showErrorModal(message) {
    var modal = document.getElementById('_errModal');
    document.getElementById('_errModalMsg').textContent = message;
    modal.style.display = 'flex';
}
