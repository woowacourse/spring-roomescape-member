document.addEventListener("DOMContentLoaded", () => {
    const state = {
        themes: [],
        dates: [],
        rankedThemes: [],
        times: [],
        selectedThemeId: null,
        selectedDateId: null
    };

    const modalTriggers = document.querySelectorAll("[data-modal-open]");
    const modalClosers = document.querySelectorAll("[data-modal-close]");
    const themeList = document.getElementById("theme-list");
    const dateList = document.getElementById("date-list");
    const rankList = document.getElementById("rank-list");
    const timeList = document.getElementById("time-list");
    const timeSection = document.getElementById("time-section");
    const selectionSummary = document.getElementById("selection-summary");
    const reservationFormSummary = document.getElementById("reservation-form-summary");
    const selectedThemeInput = document.getElementById("selected-theme-id");
    const selectedDateInput = document.getElementById("selected-date-id");
    const showTimesButton = document.getElementById("show-times-button");
    const resetSelectionButton = document.getElementById("reset-selection-button");
    const statusStrip = document.getElementById("status-strip");

    modalTriggers.forEach((trigger) => {
        trigger.addEventListener("click", () => {
            const modal = document.getElementById(trigger.dataset.modalOpen);
            if (!modal) {
                return;
            }
            modal.hidden = false;
            document.body.classList.add("modal-open");
        });
    });

    modalClosers.forEach((closer) => {
        closer.addEventListener("click", () => {
            const modal = closer.closest(".modal");
            if (!modal) {
                return;
            }
            modal.hidden = true;
            document.body.classList.remove("modal-open");
        });
    });

    document.addEventListener("keydown", (event) => {
        if (event.key !== "Escape") {
            return;
        }
        document.querySelectorAll(".modal").forEach((modal) => {
            modal.hidden = true;
        });
        document.body.classList.remove("modal-open");
    });

    function updateStatus(message) {
        statusStrip.querySelector("p").textContent = message;
    }

    function formatDate(date) {
        return date;
    }

    function formatShortDate(date) {
        return date.slice(5).replace("-", ".");
    }

    function findTheme() {
        return state.themes.find((theme) => theme.id === state.selectedThemeId);
    }

    function findDate() {
        return state.dates.find((date) => date.id === state.selectedDateId);
    }

    function renderThemes() {
        themeList.innerHTML = state.themes.map((theme) => `
            <label class="theme-card theme-card-refined${state.selectedThemeId === theme.id ? " selected" : ""}">
                <input type="radio" name="themeId" value="${theme.id}" ${state.selectedThemeId === theme.id ? "checked" : ""}>
                <img class="theme-thumbnail" src="/images/theme-placeholder.svg" alt="${theme.name}">
                <span class="theme-name">${theme.name}</span>
                <span class="theme-description">${theme.content}</span>
            </label>
        `).join("");

        themeList.querySelectorAll('input[name="themeId"]').forEach((input) => {
            input.addEventListener("change", () => {
                state.selectedThemeId = Number(input.value);
                renderThemes();
            });
        });
    }

    function renderDates() {
        dateList.innerHTML = state.dates.map((date) => `
            <label class="date-card date-card-refined${state.selectedDateId === date.id ? " selected" : ""}">
                <input type="radio" name="dateId" value="${date.id}" ${state.selectedDateId === date.id ? "checked" : ""}>
                <span class="date-day">${formatShortDate(date.playDay)}</span>
                <span class="date-full">${formatDate(date.playDay)}</span>
            </label>
        `).join("");

        dateList.querySelectorAll('input[name="dateId"]').forEach((input) => {
            input.addEventListener("change", () => {
                state.selectedDateId = Number(input.value);
                renderDates();
            });
        });
    }

    function renderRankedThemes() {
        rankList.innerHTML = state.rankedThemes.map((theme, index) => `
            <li>
                <img class="rank-thumbnail" src="/images/theme-placeholder.svg" alt="${theme.name}">
                <span class="rank-number">${index + 1}</span>
                <div>
                    <strong>${theme.name}</strong>
                </div>
            </li>
        `).join("");
    }

    function renderTimes() {
        timeList.innerHTML = state.times.map((time) => `
            <label class="time-card time-card-refined ${time.available ? "available" : "unavailable"}">
                <input type="radio" name="timeId" value="${time.timeId}" form="reservation-form" ${time.available ? "" : "disabled"}>
                <span class="card-pill">${time.available ? "OPEN" : "CLOSED"}</span>
                <span class="time-value">${time.startAt}</span>
                <span class="time-status">${time.available ? "예약 가능" : "마감"}</span>
            </label>
        `).join("");
    }

    async function fetchJson(url) {
        const response = await fetch(url, { headers: { Accept: "application/json" } });
        if (!response.ok) {
            throw new Error(`Request failed: ${url}`);
        }
        return response.json();
    }

    async function loadInitialData() {
        const [themes, dates, rankedThemes] = await Promise.all([
            fetchJson("/themes"),
            fetchJson("/reservation-dates"),
            fetchJson("/themes/rank")
        ]);

        state.themes = themes;
        state.dates = dates;
        state.rankedThemes = rankedThemes;

        renderThemes();
        renderDates();
        renderRankedThemes();
    }

    showTimesButton.addEventListener("click", async () => {
        if (!state.selectedThemeId || !state.selectedDateId) {
            updateStatus("테마와 날짜를 모두 선택해 주세요.");
            return;
        }

        const selectedTheme = findTheme();
        const selectedDate = findDate();

        state.times = await fetchJson(`/times?themeId=${state.selectedThemeId}&dateId=${state.selectedDateId}`);
        renderTimes();
        selectedThemeInput.value = String(state.selectedThemeId);
        selectedDateInput.value = String(state.selectedDateId);
        selectionSummary.textContent = `${selectedTheme.name} · ${selectedDate.playDay} 기준으로 가능한 시간입니다.`;
        reservationFormSummary.textContent = `${selectedTheme.name} · ${selectedDate.playDay}`;
        timeSection.hidden = false;
        updateStatus("가능한 시간을 불러왔습니다.");
        timeSection.scrollIntoView({ behavior: "smooth", block: "start" });
    });

    resetSelectionButton.addEventListener("click", () => {
        state.selectedThemeId = null;
        state.selectedDateId = null;
        state.times = [];
        renderThemes();
        renderDates();
        timeSection.hidden = true;
        updateStatus("테마와 날짜를 먼저 선택해 주세요.");
    });

    const reservationForm = document.getElementById("reservation-form");
    const message = document.getElementById("reservation-message");

    reservationForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(reservationForm);
        const payload = {
            name: formData.get("name"),
            themeId: Number(formData.get("themeId")),
            dateId: Number(formData.get("dateId")),
            timeId: Number(formData.get("timeId"))
        };

        message.textContent = "";
        message.className = "form-message";

        try {
            const response = await fetch("/reservations", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(payload)
            });

            const result = await response.json();

            if (!response.ok) {
                message.textContent = result.message || "예약 요청 중 문제가 발생했습니다.";
                message.classList.add("error");
                return;
            }

            message.textContent = result.name + "님의 예약이 완료되었습니다.";
            message.classList.add("success");

            window.setTimeout(() => {
                window.location.reload();
            }, 600);
        } catch (error) {
            message.textContent = "서버와 통신하지 못했습니다. 잠시 후 다시 시도해 주세요.";
            message.classList.add("error");
        }
    });

    loadInitialData().catch(() => {
        updateStatus("초기 데이터를 불러오지 못했습니다.");
    });
});
