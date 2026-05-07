const state = {
    themes: [],
    times: [],
    reservations: [],
    selectedThemeId: null,
    selectedTimeId: null,
    reservationSort: "date",
};

const fallbackImages = [
    "https://images.unsplash.com/photo-1518005020951-eccb494ad742?auto=format&fit=crop&w=900&q=80",
    "https://images.unsplash.com/photo-1509248961158-e54f6934749c?auto=format&fit=crop&w=900&q=80",
    "https://images.unsplash.com/photo-1519608487953-e999c86e7455?auto=format&fit=crop&w=900&q=80",
    "https://images.unsplash.com/photo-1560185127-6ed189bf02f4?auto=format&fit=crop&w=900&q=80",
];

const popularPeriod = 7;

const $ = (selector) => document.querySelector(selector);

document.addEventListener("DOMContentLoaded", async () => {
    $("#dateInput").value = new Date().toISOString().slice(0, 10);
    renderPopularPeriod();
    bindEvents();
    await loadAll();
});

function bindEvents() {
    document.querySelectorAll(".tab").forEach((tab) => {
        tab.addEventListener("click", () => switchView(tab.dataset.view));
    });

    $("#dateInput").addEventListener("change", loadThemeTimes);
    $("#reservationForm").addEventListener("submit", createReservation);
    $("#refreshReservationsButton").addEventListener("click", loadReservations);
    $("#reservationSortSelect").addEventListener("change", (event) => {
        state.reservationSort = event.target.value;
        renderReservations();
    });
    $("#themeForm").addEventListener("submit", createTheme);
    $("#timeForm").addEventListener("submit", createTime);
}

async function loadAll() {
    await Promise.all([
        loadThemes(),
        loadTimes(),
        loadReservations(),
        loadPopularThemes(),
    ]);
    renderAdminLists();
}

async function api(path, options = {}) {
    const response = await fetch(path, {
        headers: {
            "Content-Type": "application/json",
            ...options.headers,
        },
        ...options,
    });

    if (!response.ok) {
        let message = "요청을 처리하지 못했습니다.";
        try {
            const error = await response.json();
            message = error.message || message;
        } catch (ignore) {
            message = `${response.status} ${response.statusText}`;
        }
        throw new Error(message);
    }

    if (response.status === 204) {
        return null;
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

async function loadThemes() {
    try {
        const data = await api("/themes");
        state.themes = data.themes || [];
        if (!state.selectedThemeId && state.themes.length > 0) {
            state.selectedThemeId = state.themes[0].id;
        }
        renderThemes();
        await loadThemeTimes();
    } catch (error) {
        showToast(error.message);
    }
}

async function loadTimes() {
    try {
        state.times = await api("/times");
        renderAdminLists();
    } catch (error) {
        showToast(error.message);
    }
}

async function loadReservations() {
    try {
        state.reservations = await api("/reservations");
        renderReservations();
    } catch (error) {
        showToast(error.message);
    }
}

async function loadPopularThemes() {
    try {
        const data = await api(`/themes/popular?period=${popularPeriod}&limit=10`);
        renderPopularThemes(data.popularThemes || []);
    } catch (error) {
        $("#popularThemes").innerHTML = emptyState("인기 테마를 불러오지 못했습니다.");
    }
}

async function loadThemeTimes() {
    if (!state.selectedThemeId) {
        $("#timeSlots").innerHTML = emptyState("테마를 먼저 등록하거나 선택하세요.");
        return;
    }

    const date = $("#dateInput").value;
    if (!date) {
        return;
    }

    try {
        const data = await api(`/themes/${state.selectedThemeId}/times?date=${date}`);
        renderTimeSlots(data.times || []);
    } catch (error) {
        showToast(error.message);
    }
}

function renderThemes() {
    $("#themeCount").textContent = `${state.themes.length}개`;

    if (state.themes.length === 0) {
        $("#themeGrid").innerHTML = emptyState("등록된 테마가 없습니다. 관리 화면에서 테마를 추가하세요.");
        return;
    }

    $("#themeGrid").innerHTML = state.themes.map((theme, index) => `
        <button class="theme-card ${theme.id === state.selectedThemeId ? "selected" : ""}" type="button" data-theme-id="${theme.id}">
            <img src="${escapeHtml(theme.thumbnailUrl || fallbackImages[index % fallbackImages.length])}" alt="${escapeHtml(theme.name)} 썸네일">
            <span class="theme-card-body">
                <strong>${escapeHtml(theme.name)}</strong>
                <p>${escapeHtml(theme.description)}</p>
            </span>
        </button>
    `).join("");

    document.querySelectorAll(".theme-card").forEach((card) => {
        card.addEventListener("click", async () => {
            state.selectedThemeId = Number(card.dataset.themeId);
            state.selectedTimeId = null;
            renderThemes();
            await loadThemeTimes();
        });
    });
}

function renderTimeSlots(times) {
    if (times.length === 0) {
        $("#timeSlots").innerHTML = emptyState("등록된 시간이 없습니다.");
        return;
    }

    const availableTimes = times.filter((time) => time.isAvailable);
    if (!availableTimes.some((time) => time.id === state.selectedTimeId)) {
        state.selectedTimeId = availableTimes[0]?.id || null;
    }

    $("#timeSlots").innerHTML = times.map((time) => `
        <button class="time-button ${time.id === state.selectedTimeId ? "selected" : ""}" type="button" data-time-id="${time.id}" ${time.isAvailable ? "" : "disabled"}>
            ${formatTime(time.startAt)}
        </button>
    `).join("");

    document.querySelectorAll(".time-button:not(:disabled)").forEach((button) => {
        button.addEventListener("click", () => {
            state.selectedTimeId = Number(button.dataset.timeId);
            renderTimeSlots(times);
        });
    });
}

function renderPopularThemes(popularThemes) {
    if (popularThemes.length === 0) {
        $("#popularThemes").innerHTML = emptyState("예약 데이터가 쌓이면 순위가 표시됩니다.");
        return;
    }

    $("#popularThemes").innerHTML = popularThemes.slice(0, 10).map((theme) => `
        <div class="popular-item">
            <span class="rank">${theme.rank}</span>
            <div>
                <strong>${escapeHtml(theme.name)}</strong>
                <p>${escapeHtml(theme.description)}</p>
            </div>
        </div>
    `).join("");
}

function renderPopularPeriod() {
    const endDate = new Date();
    endDate.setDate(endDate.getDate() - 1);

    const startDate = new Date(endDate);
    startDate.setDate(startDate.getDate() - popularPeriod + 1);

    $("#popularPeriodLabel").textContent = `${formatDateLabel(startDate)} - ${formatDateLabel(endDate)}`;
}

function renderReservations() {
    if (state.reservations.length === 0) {
        $("#reservationList").innerHTML = emptyState("예약 내역이 없습니다.");
        return;
    }

    const reservations = sortReservations(state.reservations);
    $("#reservationList").innerHTML = reservations.map((reservation) => `
        <article class="reservation-item">
            <div>
                <strong>${escapeHtml(reservation.username)}</strong>
                <p>${escapeHtml(reservation.theme.name)}</p>
                <div class="reservation-meta">
                    <span class="chip">${reservation.date}</span>
                    <span class="chip">${formatTime(reservation.time.startAt)}</span>
                </div>
            </div>
            <button class="danger-button" type="button" data-reservation-id="${reservation.id}">취소</button>
        </article>
    `).join("");

    document.querySelectorAll("[data-reservation-id]").forEach((button) => {
        button.addEventListener("click", () => deleteReservation(button.dataset.reservationId));
    });
}

function sortReservations(reservations) {
    const sorted = [...reservations];

    if (state.reservationSort === "theme") {
        return sorted.sort((a, b) => {
            const themeCompare = a.theme.name.localeCompare(b.theme.name, "ko-KR");
            if (themeCompare !== 0) {
                return themeCompare;
            }
            return compareReservationDateTime(a, b);
        });
    }

    return sorted.sort(compareReservationDateTime);
}

function compareReservationDateTime(a, b) {
    return `${a.date} ${a.time.startAt}`.localeCompare(`${b.date} ${b.time.startAt}`);
}

function renderAdminLists() {
    $("#adminThemeList").innerHTML = state.themes.length === 0
        ? emptyState("등록된 테마가 없습니다.")
        : state.themes.map((theme) => `
                <div class="admin-item">
                    <div>
                        <strong>${escapeHtml(theme.name)}</strong>
                        <p>${escapeHtml(theme.description)}</p>
                    </div>
                    <button class="danger-button" type="button" data-theme-delete="${theme.id}">삭제</button>
                </div>
            `).join("");

    $("#adminTimeList").innerHTML = state.times.length === 0
        ? emptyState("등록된 시간이 없습니다.")
        : state.times.map((time) => `
                <div class="admin-item">
                    <strong>${formatTime(time.startAt)}</strong>
                    <button class="danger-button" type="button" data-time-delete="${time.id}">삭제</button>
                </div>
            `).join("");

    document.querySelectorAll("[data-theme-delete]").forEach((button) => {
        button.addEventListener("click", () => deleteTheme(button.dataset.themeDelete));
    });
    document.querySelectorAll("[data-time-delete]").forEach((button) => {
        button.addEventListener("click", () => deleteTime(button.dataset.timeDelete));
    });
}

async function createReservation(event) {
    event.preventDefault();

    const form = event.currentTarget;
    const username = $("#usernameInput").value.trim();
    const date = $("#dateInput").value;

    if (!state.selectedThemeId || !state.selectedTimeId) {
        showToast("테마와 시간을 선택하세요.");
        return;
    }

    try {
        await api("/reservations", {
            method: "POST",
            body: JSON.stringify({
                username,
                themeId: state.selectedThemeId,
                date,
                timeId: state.selectedTimeId,
            }),
        });
        form.reset();
        $("#dateInput").value = date;
        showToast("예약이 완료되었습니다.");
        await Promise.all([loadReservations(), loadThemeTimes(), loadPopularThemes()]);
    } catch (error) {
        showToast(error.message);
    }
}

async function createTheme(event) {
    event.preventDefault();
    const formElement = event.currentTarget;
    const form = new FormData(formElement);

    try {
        await api("/admin/themes", {
            method: "POST",
            body: JSON.stringify(Object.fromEntries(form)),
        });
        formElement.reset();
        showToast("테마가 추가되었습니다.");
        await Promise.all([loadThemes(), loadPopularThemes()]);
        renderAdminLists();
    } catch (error) {
        showToast(error.message);
    }
}

async function createTime(event) {
    event.preventDefault();
    const formElement = event.currentTarget;
    const form = new FormData(formElement);

    try {
        await api("/times", {
            method: "POST",
            body: JSON.stringify({startAt: form.get("startAt")}),
        });
        formElement.reset();
        showToast("시간이 추가되었습니다.");
        await loadTimes();
        await loadThemeTimes();
    } catch (error) {
        showToast(error.message);
    }
}

async function deleteReservation(id) {
    try {
        await api(`/reservations/${id}`, {method: "DELETE"});
        showToast("예약이 취소되었습니다.");
        await Promise.all([loadReservations(), loadThemeTimes(), loadPopularThemes()]);
    } catch (error) {
        showToast(error.message);
    }
}

async function deleteTheme(id) {
    try {
        await api(`/admin/themes/${id}`, {method: "DELETE"});
        if (state.selectedThemeId === Number(id)) {
            state.selectedThemeId = null;
        }
        showToast("테마가 삭제되었습니다.");
        await Promise.all([loadThemes(), loadPopularThemes()]);
        renderAdminLists();
    } catch (error) {
        showToast(error.message);
    }
}

async function deleteTime(id) {
    try {
        await api(`/times/${id}`, {method: "DELETE"});
        if (state.selectedTimeId === Number(id)) {
            state.selectedTimeId = null;
        }
        showToast("시간이 삭제되었습니다.");
        await loadTimes();
        await loadThemeTimes();
    } catch (error) {
        showToast(error.message);
    }
}

function switchView(view) {
    document.querySelectorAll(".tab").forEach((tab) => {
        tab.classList.toggle("active", tab.dataset.view === view);
    });
    document.querySelectorAll(".view").forEach((section) => section.classList.remove("active-view"));
    $(`#${view}View`).classList.add("active-view");

    if (view === "reservations") {
        loadReservations();
    }
}

function emptyState(message) {
    return `<div class="empty-state">${escapeHtml(message)}</div>`;
}

function formatTime(value) {
    return String(value || "").slice(0, 5);
}

function formatDateLabel(date) {
    return new Intl.DateTimeFormat("ko-KR", {
        month: "numeric",
        day: "numeric",
    }).format(date);
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#039;");
}

let toastTimer;

function showToast(message) {
    const toast = $("#toast");
    toast.textContent = message;
    toast.classList.add("show");
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => toast.classList.remove("show"), 2800);
}
