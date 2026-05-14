let changingReservationId = null;
let changingThemeId = null;
let selectedChangeDate = null;

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

    const response = await fetch(`/reservations?name=${encodeURIComponent(name)}`);

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
        const isPast = isPastReservation(reservation.date, reservation.time);
        const isCanceled = reservation.status === "CANCELED";
        const actionButtons = (isPast || isCanceled) ? "" : `
            <div style="display: flex; gap: 8px; margin-top: 14px;">
                <button class="cancel-button" type="button"
                    style="background: transparent; border: 1px solid #d2b44c; color: #d2b44c; border-radius: 6px; height: 36px; width: 120px; font-size: 13px; font-weight: 700; cursor: pointer;"
                    onclick="openChangeModal(${reservation.id}, ${reservation.theme.id}, '${reservation.date}')">
                    예약 변경
                </button>
                <button class="cancel-button" type="button"
                    onclick="cancelReservation(${reservation.id}, '${reservation.name}')">
                    예약 취소
                </button>
            </div>
        `;

        resultList.insertAdjacentHTML("beforeend", `
            <article class="reservation-card">
                <img src="${reservation.theme.thumbnailUrl || 'https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=800&q=80'}"
                     alt="${reservation.theme.name}">
                <div class="reservation-info">
                    <h3>${reservation.theme.name}</h3>
                    <p>예약자: ${reservation.name}</p>
                    <p>날짜: ${formatDate(reservation.date)}</p>
                    <p>시간: ${formatTime(reservation.time)}</p>
                    <p>상태: ${isCanceled ? '취소됨' : '예약중'}</p>
                    ${actionButtons}
                </div>
            </article>
        `);
    });
}

function isPastReservation(date, time) {
    return new Date(`${date}T${time}`) < new Date();
}

async function openChangeModal(reservationId, themeId, currentDate) {
    changingReservationId = reservationId;
    changingThemeId = themeId;
    selectedChangeDate = currentDate;

    const response = await fetch("/available-dates");
    if (!response.ok) return;
    const availableDates = await response.json();

    const dateList = document.getElementById("change-date-list");
    dateList.innerHTML = "";

    availableDates.forEach(dateDto => {
        const localDate = new Date(dateDto.date);
        const month = localDate.toLocaleString("en-US", { month: "short" }).toUpperCase();
        const day = localDate.getDate();

        const button = document.createElement("button");
        button.type = "button";
        button.dataset.date = dateDto.date;
        button.style.cssText = `
            width: 60px; height: 60px; border-radius: 7px;
            border: 1px solid #1e2228; background: #0b0d10;
            color: #a9a9a9; cursor: pointer; font-size: 12px;
        `;
        button.innerHTML = `
            <span style="display:block; font-size:11px; font-weight:800;">${month}</span>
            <span style="display:block; font-size:20px; font-family:Georgia,serif;">${day}</span>
        `;

        if (dateDto.date === currentDate) {
            button.style.borderColor = "#d2b44c";
            button.style.background = "rgba(210,180,76,0.12)";
            button.style.color = "#ffffff";
        }

        button.addEventListener("click", async () => {
            document.querySelectorAll("#change-date-list button").forEach(btn => {
                btn.style.borderColor = "#1e2228";
                btn.style.background = "#0b0d10";
                btn.style.color = "#a9a9a9";
            });
            button.style.borderColor = "#d2b44c";
            button.style.background = "rgba(210,180,76,0.12)";
            button.style.color = "#ffffff";
            selectedChangeDate = dateDto.date;
            await loadAvailableTimes(dateDto.date, themeId);
        });

        dateList.appendChild(button);
    });

    await loadAvailableTimes(currentDate, themeId);

    document.getElementById("change-error-message").classList.add("hidden");
    document.getElementById("change-modal").classList.add("active");
}

function closeChangeModal() {
    document.getElementById("change-modal").classList.remove("active");
    changingReservationId = null;
    changingThemeId = null;
    selectedChangeDate = null;
}

async function loadAvailableTimes(date, themeId) {
    if (!date || !themeId) return;

    const response = await fetch(`/times?date=${date}&themeId=${themeId}`);
    if (!response.ok) return;

    const times = await response.json();
    const select = document.getElementById("change-time-select");
    select.innerHTML = "";

    if (times.length === 0) {
        select.innerHTML = `<option disabled>예약 가능한 시간이 없습니다.</option>`;
        return;
    }

    times.forEach(time => {
        const option = document.createElement("option");
        option.value = time.id;
        option.textContent = formatTime(time.startAt);
        select.appendChild(option);
    });
}

async function confirmChange() {
    const timeId = document.getElementById("change-time-select").value;
    const errorMessage = document.getElementById("change-error-message");

    if (!selectedChangeDate || !timeId) {
        errorMessage.textContent = "날짜와 시간을 선택해주세요.";
        errorMessage.classList.remove("hidden");
        return;
    }

    const response = await fetch(`/reservations/${changingReservationId}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ date: selectedChangeDate, timeId: Number(timeId) })
    });

    if (!response.ok) {
        const error = await response.json();
        errorMessage.textContent = error.message;
        errorMessage.classList.remove("hidden");
        return;
    }

    closeChangeModal();
    alert("예약이 변경되었습니다.");
    await searchReservations();
}

async function cancelReservation(id, name) {
    if (!confirm("해당 예약을 취소하시겠습니까?")) return;

    const response = await fetch(`/reservations/${id}/cancel`, {
        method: "PATCH"
    });

    if (!response.ok) {
        const error = await response.json();
        alert(error.message);
        return;
    }

    alert("예약이 취소되었습니다.");
    document.getElementById("lookup-name-input").value = name;
    await searchReservations();
}

function formatDate(value) {
    if (!value) return "";
    const date = new Date(value);
    return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`;
}

function formatTime(value) {
    if (!value) return "";
    const parts = value.split(":");
    return `${parts[0]}:${parts[1]}`;
}
