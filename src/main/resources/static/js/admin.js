document.addEventListener("DOMContentLoaded", () => {
    initTabs();
    initDatePicker();
    initTimeSelectBox();

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
    const dates = await response.json();

    const tbody = document.getElementById("date-table-body");
    tbody.innerHTML = "";

    dates.forEach(date => {
        tbody.insertAdjacentHTML("beforeend", `
            <tr>
                <td>${date.id}</td>
                <td>${date.date}</td>
                <td class="align-right">
                    <button class="delete-button" type="button" onclick="deleteDate(${date.id})">
                        🗑
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

    await fetch("/admin/dates", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ date })
    });

    dateInput.value = "";
    selectedDateText.textContent = "날짜를 선택하세요";

    await loadDates();
}

async function deleteDate(id) {
    if (!confirm("해당 날짜를 삭제하시겠습니까?")) {
        return;
    }

    await fetch(`/admin/dates/${id}`, {
        method: "DELETE"
    });

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
