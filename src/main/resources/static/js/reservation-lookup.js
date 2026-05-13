let searchedName = null;

document.addEventListener("DOMContentLoaded", async () => {
    const params = new URLSearchParams(window.location.search);
    const name = params.get("name");

    if (name) {
        const input = document.getElementById("lookup-name-input");
        input.value = name;
        await searchReservations();
    }
});

async function searchReservations() {
    const name = document.getElementById("lookup-name-input").value.trim();

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

    searchedName = name;

    document.getElementById("lookup-name-text").textContent = name;
    document.getElementById("lookup-result-section").classList.remove("hidden");

    renderReservations(reservations);
}

function renderReservations(reservations) {
    const reservationResultList = document.getElementById("reservation-result-list");
    reservationResultList.innerHTML = "";

    if (reservations.length === 0) {
        reservationResultList.innerHTML = `
            <div class="lookup-empty-message">
                예약 내역이 없습니다.
            </div>
        `;
        return;
    }

    reservations.forEach(reservation => {
        const article = document.createElement("article");
        article.className = "reservation-result-card";

        const isCanceled = reservation.status === "CANCELED";
        const thumbnailUrl = getThemeThumbnailUrl(reservation);

        article.innerHTML = `
            <div class="reservation-thumbnail-box">
                <img
                    class="reservation-thumbnail"
                    src="${thumbnailUrl}"
                    alt="${getThemeName(reservation)}"
                >
            </div>

            <div class="reservation-result-info">
                <h3>${getThemeName(reservation)}</h3>
                <p>예약자: ${reservation.name}</p>
                <p>날짜: ${getReservationDate(reservation)}</p>
                <p>시간: ${formatTime(getReservationTime(reservation))}</p>
                <p>상태: ${formatStatus(reservation.status)}</p>

                <button
                    class="cancel-button"
                    type="button"
                    ${isCanceled ? "disabled" : ""}
                >
                    ${isCanceled ? "취소 완료" : "예약 취소"}
                </button>
            </div>
        `;

        const cancelButton = article.querySelector(".cancel-button");

        if (!isCanceled) {
            cancelButton.addEventListener("click", async () => {
                await cancelReservation(reservation.id);
            });
        }

        reservationResultList.appendChild(article);
    });
}

function getThemeThumbnailUrl(reservation) {
    if (reservation.theme && reservation.theme.thumbnailUrl) {
        return reservation.theme.thumbnailUrl;
    }

    if (reservation.themeThumbnailUrl) {
        return reservation.themeThumbnailUrl;
    }

    if (reservation.thumbnailUrl) {
        return reservation.thumbnailUrl;
    }

    return "";
}

async function cancelReservation(reservationId) {
    if (!searchedName) {
        alert("예약자 성함으로 다시 조회해주세요.");
        return;
    }

    const confirmed = confirm("예약을 취소하시겠습니까?");

    if (!confirmed) {
        return;
    }

    const response = await fetch(`/member/reservations/${reservationId}/cancel`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: searchedName
        })
    });

    if (!response.ok) {
        alert("예약 취소에 실패했습니다.");
        return;
    }

    alert("예약이 취소되었습니다.");

    await reloadReservationsBySearchedName();
}

async function reloadReservationsBySearchedName() {
    if (!searchedName) {
        return;
    }

    const response = await fetch(`/member/reservations/${encodeURIComponent(searchedName)}`);

    if (!response.ok) {
        alert("예약 내역을 다시 불러오지 못했습니다.");
        return;
    }

    const reservations = await response.json();

    document.getElementById("lookup-name-text").textContent = searchedName;
    document.getElementById("lookup-result-section").classList.remove("hidden");

    renderReservations(reservations);
}

function getThemeName(reservation) {
    if (reservation.theme && reservation.theme.name) {
        return reservation.theme.name;
    }

    if (reservation.themeName) {
        return reservation.themeName;
    }

    return "테마 정보 없음";
}

function getReservationDate(reservation) {
    if (reservation.date && reservation.date.date) {
        return reservation.date.date;
    }

    if (reservation.reservationDate && reservation.reservationDate.date) {
        return reservation.reservationDate.date;
    }

    if (reservation.date) {
        return reservation.date;
    }

    return "";
}

function getReservationTime(reservation) {
    if (reservation.time && reservation.time.startAt) {
        return reservation.time.startAt;
    }

    if (reservation.reservationTime && reservation.reservationTime.startAt) {
        return reservation.reservationTime.startAt;
    }

    if (reservation.startAt) {
        return reservation.startAt;
    }

    return "";
}

function formatTime(value) {
    if (!value) {
        return "";
    }

    const parts = value.split(":");
    return `${parts[0]}:${parts[1]}`;
}

function formatStatus(status) {
    if (status === "RESERVED") {
        return "예약 완료";
    }

    if (status === "CANCELED") {
        return "예약 취소";
    }

    return status;
}
