document.addEventListener("DOMContentLoaded", () => {
    initTabs();
    initDatePicker();
    initTimeSelectBox();

    loadPopularThemes();
    loadDates();
    loadTimes();
    loadThemes();
    loadReservations();
});

function initTabs() {
    const tabButtons = document.querySelectorAll(".tab-button");
    const panels = document.querySelectorAll(".panel");

    tabButtons.forEach(button => {
        button.addEventListener("click", () => {
            tabButtons.forEach(tab => tab.classList.remove("active"));
            panels.forEach(panel => panel.classList.remove("active"));

            button.classList.add("active");

            const panelId = button.dataset.panel;
            document.getElementById(panelId).classList.add("active");
        });
    });
}

function initDatePicker() {
    const dateInput = document.getElementById("date-input");
    const datePickerButton = document.getElementById("date-picker-button");
    const selectedDateText = document.getElementById("selected-date-text");

    datePickerButton.addEventListener("click", () => {
        if (dateInput.showPicker) {
            dateInput.showPicker();
            return;
        }

        dateInput.click();
    });

    dateInput.addEventListener("change", () => {
        if (!dateInput.value) {
            selectedDateText.textContent = "날짜를 선택하세요";
            return;
        }

        selectedDateText.textContent = dateInput.value;
    });
}

function initTimeSelectBox() {
    const hourSelect = document.getElementById("hour-select");
    const minuteSelect = document.getElementById("minute-select");

    for (let hour = 0; hour < 24; hour++) {
        const option = document.createElement("option");
        option.value = String(hour).padStart(2, "0");
        option.textContent = `${hour}시`;
        hourSelect.appendChild(option);
    }

    for (let minute = 0; minute < 60; minute += 10) {
        const option = document.createElement("option");
        option.value = String(minute).padStart(2, "0");
        option.textContent = `${minute}분`;
        minuteSelect.appendChild(option);
    }

    hourSelect.value = "12";
    minuteSelect.value = "00";
}

// 날짜 관리
async function loadDates() {
    const response = await fetch("/admin/dates");

    if (!response.ok) {
        alert("날짜 목록을 불러오지 못했습니다.");
        return;
    }

    const dates = await response.json();

    const tbody = document.getElementById("date-table-body");
    tbody.innerHTML = "";

    dates.forEach(date => {
        const badgeClass = date.isActive ? "active" : "inactive";
        const badgeText = date.isActive ? "활성" : "비활성";
        const nextStatus = !date.isActive;
        const buttonText = date.isActive ? "비활성화" : "활성화";

        tbody.insertAdjacentHTML("beforeend", `
            <tr>
                <td>${date.id}</td>
                <td>${date.date}</td>
                <td>
                    <span class="badge ${badgeClass}">${badgeText}</span>
                </td>
                <td class="align-right">
                    <button class="status-button" type="button"
                            onclick="updateDateStatus(${date.id}, ${nextStatus})">
                        ${buttonText}
                    </button>
                </td>
            </tr>
        `);
    });
}

async function createDate() {
    const dateInput = document.getElementById("date-input");
    const selectedDateText = document.getElementById("selected-date-text");
    const date = dateInput.value;

    if (!date) {
        alert("날짜를 선택해주세요.");
        return;
    }

    const response = await fetch("/admin/dates", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ date })
    });

    if (!response.ok) {
        alert("날짜 추가에 실패했습니다.");
        return;
    }

    dateInput.value = "";
    selectedDateText.textContent = "날짜를 선택하세요";

    await loadDates();
}

async function updateDateStatus(id, isActive) {
    const message = isActive
        ? "해당 날짜를 활성화하시겠습니까?"
        : "해당 날짜를 비활성화하시겠습니까?";

    if (!confirm(message)) {
        return;
    }

    const response = await fetch(`/admin/dates/${id}/status`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ isActive })
    });

    if (!response.ok) {
        alert("날짜 상태 변경에 실패했습니다.");
        return;
    }

    await loadDates();
}

// 시간 관리
async function loadTimes() {
    const response = await fetch("/admin/times");
    const times = await response.json();

    const tbody = document.getElementById("time-table-body");
    tbody.innerHTML = "";

    times.forEach(time => {
        tbody.insertAdjacentHTML("beforeend", `
            <tr>
                <td>${time.id}</td>
                <td>${formatTime(time.startAt)}</td>
                <td class="align-right">
                    <button class="delete-button" type="button" onclick="deleteTime(${time.id})">
                        🗑
                    </button>
                </td>
            </tr>
        `);
    });
}

async function createTime() {
    const hour = document.getElementById("hour-select").value;
    const minute = document.getElementById("minute-select").value;
    const startAt = `${hour}:${minute}:00`;

    await fetch("/admin/times", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ startAt })
    });

    await loadTimes();
}

async function deleteTime(id) {
    if (!confirm("해당 시간을 삭제하시겠습니까?")) {
        return;
    }

    await fetch(`/admin/times/${id}`, {
        method: "DELETE"
    });

    await loadTimes();
}

function formatTime(value) {
    if (!value) {
        return "";
    }

    const parts = value.split(":");
    const hour = parts[0];
    const minute = parts[1];
    const second = parts[2] ?? "00";

    return `${hour}:${minute}:${second}`;
}

// 테마 관리
async function loadThemes() {
    const response = await fetch("/admin/themes");
    const themes = await response.json();

    const tbody = document.getElementById("theme-table-body");
    tbody.innerHTML = "";

    themes.forEach(theme => {
        const badgeClass = theme.isActive ? "active" : "inactive";
        const badgeText = theme.isActive ? "활성" : "비활성";

        tbody.insertAdjacentHTML("beforeend", `
            <tr>
                <td>${theme.id}</td>
                <td>${theme.name}</td>
                <td>
                    <span class="badge ${badgeClass}">${badgeText}</span>
                </td>
                <td class="align-right">
                    <button class="status-button" type="button"
                            onclick="toggleThemeStatus(${theme.id}, ${!theme.isActive})">
                        상태 변경
                    </button>
                </td>
            </tr>
        `);
    });
}

async function createTheme() {
    const name = document.getElementById("theme-name-input").value;
    const description = document.getElementById("theme-description-input").value;
    const thumbnailUrl = document.getElementById("theme-thumbnail-input").value;

    if (!name || !description || !thumbnailUrl) {
        alert("테마 정보를 모두 입력해주세요.");
        return;
    }

    await fetch("/admin/themes", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name,
            description,
            thumbnailUrl
        })
    });

    document.getElementById("theme-name-input").value = "";
    document.getElementById("theme-description-input").value = "";
    document.getElementById("theme-thumbnail-input").value = "";

    await loadThemes();
}

async function toggleThemeStatus(id, isActive) {
    await fetch(`/admin/themes/${id}`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ isActive })
    });

    await loadThemes();
}

// 예약 조회
async function loadReservations() {
    const response = await fetch("/admin/reservations");
    const reservations = await response.json();

    const tbody = document.getElementById("reservation-table-body");
    tbody.innerHTML = "";

    reservations.forEach(reservation => {
        tbody.insertAdjacentHTML("beforeend", `
            <tr>
                <td>${reservation.id}</td>
                <td>${reservation.name}</td>
                <td>${reservation.date}</td>
                <td>${formatTime(reservation.time)}</td>
                <td>${reservation.theme.name}</td>
                <td>${reservation.status}</td>
                <td class="align-right">
                    <button class="status-button" type="button" onclick="cancelReservation(${reservation.id})">
                        취소
                    </button>
                </td>
            </tr>
        `);
    });
}

async function cancelReservation(id) {
    if (!confirm("해당 예약을 취소하시겠습니까?")) {
        return;
    }

    await fetch(`/admin/reservations/${id}/cancel`, {
        method: "PATCH"
    });

    await loadReservations();
}

async function loadPopularThemes() {
    const popularThemeList = document.getElementById("popular-theme-list");

    const response = await fetch("/admin/themes/popular?top=10");

    if (!response.ok) {
        popularThemeList.innerHTML = `
            <div class="popular-admin-empty">
                인기 테마를 불러오지 못했습니다.
            </div>
        `;
        return;
    }

    const themes = await response.json();
    popularThemeList.innerHTML = "";

    if (themes.length === 0) {
        popularThemeList.innerHTML = `
            <div class="popular-admin-empty">
                아직 인기 테마 데이터가 없습니다.
            </div>
        `;
        return;
    }

    themes.forEach((theme, index) => {
        const statusClass = theme.isActive ? "active" : "inactive";
        const statusText = theme.isActive ? "활성" : "비활성";

        popularThemeList.insertAdjacentHTML("beforeend", `
            <article class="popular-admin-card">
                <div class="popular-admin-rank">${index + 1}</div>

                <img src="${theme.thumbnailUrl}" alt="${theme.name}">

                <div class="popular-admin-content">
                    <div class="popular-admin-title-row">
                        <h3>${theme.name}</h3>
                        <span class="badge ${statusClass}">${statusText}</span>
                    </div>

                    <p>${theme.description}</p>

                    <div class="popular-admin-count">
                        <span>예약 횟수</span>
                        <strong>${theme.reservationCount}</strong>
                    </div>
                </div>
            </article>
        `);
    });
}
