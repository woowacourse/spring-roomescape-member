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
        const message = await extractErrorMessage(response);
        throw new Error(message || "요청 처리 중 문제가 발생했습니다.");
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

async function extractErrorMessage(response) {
    const contentType = response.headers.get("content-type") ?? "";

    if (contentType.includes("application/json")) {
        try {
            const body = await response.json();
            if (body?.message) {
                return body.message;
            }
        } catch (error) {
            return "요청 처리 중 문제가 발생했습니다.";
        }
    }

    const message = await response.text();
    return message || "요청 처리 중 문제가 발생했습니다.";
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

const myReservationsForm = document.getElementById("my-reservations-form");
const myReservationsNameInput = document.getElementById("my-reservations-name");
const myReservationsList = document.getElementById("my-reservations-list");
const myReservationsFeedback = document.getElementById("my-reservations-feedback");

let myReservationsCurrentName = "";

function renderEmptyMyReservations(message) {
    myReservationsList.classList.add("empty-state");
    myReservationsList.textContent = message;
}

async function loadMyReservations(name) {
    clearFeedback(myReservationsFeedback);
    try {
        const data = await request(
            `/reservations/mine?name=${encodeURIComponent(name)}`,
            { method: "GET" }
        );
        myReservationsCurrentName = name;
        renderMyReservations(data);
    } catch (error) {
        renderEmptyMyReservations("내 예약을 불러오지 못했습니다.");
        showFeedback(myReservationsFeedback, "error", error.message);
    }
}

function renderMyReservations(reservations) {
    myReservationsList.innerHTML = "";

    if (reservations.length === 0) {
        renderEmptyMyReservations("조회된 예약이 없습니다.");
        return;
    }

    myReservationsList.classList.remove("empty-state");
    reservations.forEach((reservation) => {
        myReservationsList.appendChild(createReservationCard(reservation));
    });
}

function createReservationCard(reservation) {
    const card = document.createElement("article");
    card.className = "reservation-card";

    const info = document.createElement("div");
    info.className = "reservation-info";
    const themeLine = document.createElement("strong");
    themeLine.textContent = reservation.theme.name;
    const scheduleLine = document.createElement("span");
    scheduleLine.textContent = `${reservation.date} ${formatTime(reservation.time.startAt)}`;
    info.append(themeLine, scheduleLine);

    const actions = document.createElement("div");
    actions.className = "reservation-actions";

    const editButton = document.createElement("button");
    editButton.type = "button";
    editButton.className = "button ghost";
    editButton.textContent = "변경";

    const cancelButton = document.createElement("button");
    cancelButton.type = "button";
    cancelButton.className = "button danger";
    cancelButton.textContent = "취소";
    cancelButton.addEventListener("click", () => cancelMyReservation(reservation));

    actions.append(editButton, cancelButton);

    const editForm = createEditForm(reservation);
    editForm.hidden = true;
    editButton.addEventListener("click", () => {
        editForm.hidden = !editForm.hidden;
    });

    card.append(info, actions, editForm);
    return card;
}

function createEditForm(reservation) {
    const form = document.createElement("form");
    form.className = "edit-form";

    const dateField = document.createElement("label");
    dateField.className = "field";
    const dateSpan = document.createElement("span");
    dateSpan.textContent = "새 날짜";
    const dateInput = document.createElement("input");
    dateInput.type = "date";
    dateInput.required = true;
    dateInput.value = reservation.date;
    dateField.append(dateSpan, dateInput);

    const timeField = document.createElement("label");
    timeField.className = "field";
    const timeSpan = document.createElement("span");
    timeSpan.textContent = "새 시간";
    const timeSelect = document.createElement("select");
    timeSelect.required = true;
    const placeholderOption = document.createElement("option");
    placeholderOption.value = "";
    placeholderOption.textContent = "날짜를 먼저 선택하세요";
    timeSelect.append(placeholderOption);
    timeField.append(timeSpan, timeSelect);

    const submit = document.createElement("button");
    submit.type = "submit";
    submit.className = "button primary";
    submit.textContent = "저장";

    form.append(dateField, timeField, submit);

    dateInput.addEventListener("change", () =>
        loadAvailableTimesForEdit(reservation.theme.id, dateInput.value, timeSelect)
    );

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        await submitEdit(reservation.id, dateInput.value, timeSelect.value);
    });

    loadAvailableTimesForEdit(reservation.theme.id, dateInput.value, timeSelect);

    return form;
}

async function loadAvailableTimesForEdit(themeId, date, timeSelect) {
    timeSelect.innerHTML = "";
    if (!date) {
        const option = document.createElement("option");
        option.value = "";
        option.textContent = "날짜를 먼저 선택하세요";
        timeSelect.append(option);
        return;
    }
    try {
        const data = await request(
            `/times/available?themeId=${themeId}&date=${date}`,
            { method: "GET" }
        );
        const times = data.times ?? [];
        if (times.length === 0) {
            const option = document.createElement("option");
            option.value = "";
            option.textContent = "가능한 시간이 없습니다";
            timeSelect.append(option);
            return;
        }
        times.forEach((time) => {
            const option = document.createElement("option");
            option.value = time.id;
            option.textContent = formatTime(time.startAt);
            timeSelect.append(option);
        });
    } catch (error) {
        showFeedback(myReservationsFeedback, "error", error.message);
    }
}

async function submitEdit(reservationId, date, timeId) {
    clearFeedback(myReservationsFeedback);
    if (!timeId) {
        showFeedback(myReservationsFeedback, "error", "변경할 시간을 선택하세요.");
        return;
    }
    const payload = {
        name: myReservationsCurrentName,
        date: date,
        timeId: Number(timeId)
    };
    try {
        await request(`/reservations/${reservationId}`, {
            method: "PATCH",
            body: JSON.stringify(payload)
        });
        showFeedback(myReservationsFeedback, "success", "예약이 변경되었습니다.");
        await loadMyReservations(myReservationsCurrentName);
    } catch (error) {
        showFeedback(myReservationsFeedback, "error", error.message);
    }
}

async function cancelMyReservation(reservation) {
    clearFeedback(myReservationsFeedback);
    const confirmed = window.confirm(
        `${reservation.date} ${formatTime(reservation.time.startAt)} 예약을 취소할까요?`
    );
    if (!confirmed) {
        return;
    }
    try {
        await request(
            `/reservations/${reservation.id}?name=${encodeURIComponent(myReservationsCurrentName)}`,
            { method: "DELETE" }
        );
        showFeedback(myReservationsFeedback, "success", "예약이 취소되었습니다.");
        await loadMyReservations(myReservationsCurrentName);
    } catch (error) {
        showFeedback(myReservationsFeedback, "error", error.message);
    }
}

myReservationsForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const name = myReservationsNameInput.value.trim();
    if (!name) {
        showFeedback(myReservationsFeedback, "error", "이름을 입력하세요.");
        return;
    }
    await loadMyReservations(name);
});
