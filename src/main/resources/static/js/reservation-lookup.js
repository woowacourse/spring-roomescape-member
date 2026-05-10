document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const name = params.get("name");

    if (name) {
        document.getElementById("lookup-name-input").value = name;
        searchReservations();
    }
});

async function searchReservations() {
    const nameInput = document.getElementById("lookup-name-input");
    const name = nameInput.value.trim();

    if (!name) {
        alert("예약자 성함을 입력해주세요.");
        return;
    }

    const response = await fetch(`/member/reservations/${encodeURIComponent(name)}`);

    if (!response.ok) {
        alert("예약 내역을 불러오지 못했습니다.");
        return;
    }

    const reservations = await response.json();

    const resultSection = document.getElementById("lookup-result-section");
    const nameText = document.getElementById("lookup-name-text");
    const resultList = document.getElementById("reservation-result-list");

    resultSection.classList.remove("hidden");
    nameText.textContent = `"${name}"`;
    resultList.innerHTML = "";

    if (reservations.length === 0) {
        resultList.innerHTML = `<p style="color: #b5b5b5;">예약 내역이 없습니다.</p>`;
        return;
    }

    reservations.forEach(reservation => {
        resultList.insertAdjacentHTML("beforeend", `
            <article class="reservation-card">
                <img src="${reservation.theme.thumbnailUrl}" alt="${reservation.theme.name}">
                <div class="reservation-info">
                    <h3>${reservation.theme.name}</h3>
                    <p>예약자: ${reservation.name}</p>
                    <p>날짜: ${formatDate(reservation.date)}</p>
                    <p>시간: ${formatTime(reservation.time)}</p>
                    <p>상태: ${reservation.status}</p>
                    <button class="cancel-button" type="button" onclick="cancelReservation(${reservation.id}, '${reservation.name}')">
                        예약 취소
                    </button>
                </div>
            </article>
        `);
    });
}

async function cancelReservation(id, name) {
    if (!confirm("해당 예약을 취소하시겠습니까?")) {
        return;
    }

    const response = await fetch(`/member/reservations/${id}/cancel`, {
        method: "PATCH"
    });

    if (!response.ok) {
        alert("예약 취소에 실패했습니다.");
        return;
    }

    alert("예약이 취소되었습니다.");
    document.getElementById("lookup-name-input").value = name;
    await searchReservations();
}

function formatDate(value) {
    if (!value) {
        return "";
    }

    const date = new Date(value);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();

    return `${year}년 ${month}월 ${day}일`;
}

function formatTime(value) {
    if (!value) {
        return "";
    }

    const parts = value.split(":");
    return `${parts[0]}:${parts[1]}`;
}
