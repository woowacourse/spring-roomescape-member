const reservationForm = document.getElementById("reservation-form");
const reservationNameInput = document.getElementById("reservation-name");
const reservationDateInput = document.getElementById("reservation-date");
const themeSelect = document.getElementById("theme-select");
const availableTimesContainer = document.getElementById("available-times");
const availabilitySummary = document.getElementById("availability-summary");
const reservationFeedback = document.getElementById("reservation-feedback");
const reservationSubmitButton = document.getElementById("reservation-submit");

let selectedTimeId = null;
let availableTimes = [];
const checkForm = document.getElementById("check-form");
const checkNameInput = document.getElementById("check-name");
const myReservationList = document.getElementById("my-reservation-list");
const checkFeedback = document.getElementById("check-feedback");

// 예약 조회 렌더링 함수
function renderMyReservations(reservations, username) {
    if (reservations.length === 0) {
        myReservationList.innerHTML = '<div class="empty-card">조회된 예약이 없습니다.</div>';
        return;
    }

    myReservationList.innerHTML = reservations.map((reservation) => `
        <article style="background: white; border: 1px solid #e2e8f0; padding: 1rem; border-radius: 8px; margin-bottom: 0.5rem; display: flex; justify-content: space-between; align-items: center;">
            <div>
                <strong>${reservation.theme.name}</strong>
                <p style="margin: 0.25rem 0 0; color: #64748b; font-size: 0.875rem;">
                    ${reservation.date} · ${formatTime(reservation.time.startAt)}
                </p>
            </div>
            <button class="button danger" type="button" data-action="cancel-my-reservation" data-id="${reservation.id}" data-name="${username}">
                취소
            </button>
        </article>
    `).join("");
}

// 1. 조회 폼 제출 이벤트
checkForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    clearFeedback(checkFeedback);
    const name = checkNameInput.value.trim();

    try {
        // GET /reservations/{username} API 호출
        const reservations = await request(`/reservations/${name}`, { method: "GET" });
        renderMyReservations(reservations, name);
    } catch (error) {
        showFeedback(checkFeedback, "error", error.message);
    }
});

// 2. 예약 취소 버튼 클릭 이벤트 (이벤트 위임)
myReservationList.addEventListener("click", async (event) => {
    const target = event.target.closest('[data-action="cancel-my-reservation"]');
    if (!target) return;

    if (!window.confirm("이 예약을 정말 취소하시겠습니까?")) return;

    clearFeedback(checkFeedback);
    const id = target.dataset.id;
    const username = target.dataset.name;

    try {
        // DELETE /reservations/{id}?username={username} API 호출
        await request(`/reservations/${id}?username=${username}`, { method: "DELETE" });
        showFeedback(checkFeedback, "success", "예약이 성공적으로 취소되었습니다.");

        // 취소 후 목록 다시 불러오기
        const reservations = await request(`/reservations/${username}`, { method: "GET" });
        renderMyReservations(reservations, username);

        // 취소된 자리가 생겼을 수 있으므로 예약 가능 시간표도 리로드
        await loadAvailableTimes();
    } catch (error) {
        showFeedback(checkFeedback, "error", error.message);
    }
});

function showFeedback(element, type, message) {
    element.hidden = false;
    element.className = `feedback ${type}`;
    element.textContent = message;
}

function clearFeedback(element) {
    element.hidden = true;
    element.className = "feedback";
    element.textContent = "";
}

async function request(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers ?? {})
        },
        ...options
    });

    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || "요청 처리 중 문제가 발생했습니다.");
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

function formatTime(time) {
    return (time ?? "").slice(0, 5);
}

function updateSubmitState() {
    reservationSubmitButton.disabled = !selectedTimeId;
}

function renderAvailableTimes() {
    availableTimesContainer.innerHTML = "";

    if (availableTimes.length === 0) {
        availableTimesContainer.classList.add("empty-state");
        availableTimesContainer.textContent = "선택한 날짜와 테마에 예약 가능한 시간이 없습니다.";
        selectedTimeId = null;
        updateSubmitState();
        return;
    }

    availableTimesContainer.classList.remove("empty-state");

    availableTimes.forEach((time) => {
        const button = document.createElement("button");
        button.type = "button";
        button.className = `time-chip${selectedTimeId === time.id ? " selected" : ""}`;
        button.textContent = formatTime(time.startAt);
        button.addEventListener("click", () => {
            selectedTimeId = time.id;
            renderAvailableTimes();
            updateSubmitState();
        });
        availableTimesContainer.appendChild(button);
    });
}

async function loadAvailableTimes() {
    clearFeedback(reservationFeedback);

    const themeId = themeSelect.value;
    const date = reservationDateInput.value;

    if (!themeId || !date) {
        availableTimes = [];
        selectedTimeId = null;
        availabilitySummary.textContent = "날짜와 테마를 선택하면 예약 가능한 시간이 표시됩니다.";
        availableTimesContainer.classList.add("empty-state");
        availableTimesContainer.textContent = "아직 조회된 시간이 없습니다.";
        updateSubmitState();
        return;
    }

    availabilitySummary.textContent = "예약 가능 시간을 조회하는 중입니다.";

    try {
        const data = await request(`/times/available?themeId=${themeId}&date=${date}`, {
            method: "GET"
        });
        availableTimes = data.times ?? [];
        selectedTimeId = null;
        availabilitySummary.textContent =
            `${data.theme.name} 테마의 ${date} 예약 가능 시간 ${availableTimes.length}개`;
        renderAvailableTimes();
    } catch (error) {
        availableTimes = [];
        selectedTimeId = null;
        availableTimesContainer.classList.add("empty-state");
        availableTimesContainer.textContent = "예약 가능 시간을 불러오지 못했습니다.";
        availabilitySummary.textContent = "잠시 후 다시 시도하세요.";
        showFeedback(reservationFeedback, "error", error.message);
        updateSubmitState();
    }
}

reservationForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    clearFeedback(reservationFeedback);

    if (!selectedTimeId) {
        showFeedback(reservationFeedback, "error", "예약 시간을 먼저 선택하세요.");
        return;
    }

    const payload = {
        name: reservationNameInput.value.trim(),
        date: reservationDateInput.value,
        timeId: Number(selectedTimeId),
        themeId: Number(themeSelect.value)
    };

    try {
        await request("/reservations", {
            method: "POST",
            body: JSON.stringify(payload)
        });
        showFeedback(reservationFeedback, "success", "예약이 등록되었습니다.");
        reservationNameInput.value = "";
        await loadAvailableTimes();
    } catch (error) {
        showFeedback(reservationFeedback, "error", error.message);
    }
});

themeSelect.addEventListener("change", loadAvailableTimes);
reservationDateInput.addEventListener("change", loadAvailableTimes);

if (themeSelect.options.length > 1 && !themeSelect.value) {
    themeSelect.selectedIndex = 1;
}

loadAvailableTimes();
