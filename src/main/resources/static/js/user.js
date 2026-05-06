let selectedDate = null;
let selectedTheme = null;
let selectedTime = null;

document.addEventListener("DOMContentLoaded", () => {
    loadDates();
    loadThemes();
});

async function loadDates() {
    const response = await fetch("/member/dates");

    if (!response.ok) {
        alert("날짜 목록을 불러오지 못했습니다.");
        return;
    }

    const dates = await response.json();

    const dateList = document.getElementById("date-list");
    dateList.innerHTML = "";

    dates.forEach(date => {
        const localDate = new Date(date.date);
        const month = localDate.toLocaleString("en-US", { month: "short" }).toUpperCase();
        const day = localDate.getDate();

        const button = document.createElement("button");
        button.className = "date-card";
        button.type = "button";
        button.innerHTML = `
            <span class="month">${month}</span>
            <span class="day">${day}</span>
        `;

        button.addEventListener("click", () => {
            document.querySelectorAll(".date-card")
                .forEach(item => item.classList.remove("selected"));

            button.classList.add("selected");
            selectedDate = date;
        });

        dateList.appendChild(button);
    });
}

async function loadThemes() {
    const response = await fetch("/member/themes");

    if (!response.ok) {
        alert("테마 목록을 불러오지 못했습니다.");
        return;
    }

    const themes = await response.json();

    const themeList = document.getElementById("theme-list");
    themeList.innerHTML = "";

    themes.forEach(theme => {
        const article = document.createElement("article");
        article.className = "theme-card";
        article.innerHTML = `
            <img src="${theme.thumbnailUrl}" alt="${theme.name}">
            <div class="theme-card-content">
                <h3>${theme.name}</h3>
                <p>${theme.description}</p>
            </div>
        `;

        article.addEventListener("click", () => {
            document.querySelectorAll(".theme-card")
                .forEach(item => item.classList.remove("selected"));

            article.classList.add("selected");
            selectedTheme = theme;
        });

        themeList.appendChild(article);
    });
}

async function goToReservationStep() {
    if (!selectedDate) {
        alert("날짜를 선택해주세요.");
        return;
    }

    if (!selectedTheme) {
        alert("테마를 선택해주세요.");
        return;
    }

    document.getElementById("select-section").classList.add("hidden");
    document.getElementById("confirm-section").classList.remove("hidden");

    await loadAvailableTimes();
}

function goBackToSelectStep() {
    document.getElementById("confirm-section").classList.add("hidden");
    document.getElementById("select-section").classList.remove("hidden");

    selectedTime = null;
    document.getElementById("reservation-name-input").value = "";
}

async function loadAvailableTimes() {
    const response = await fetch(`/member/times?date=${selectedDate.date}&themeId=${selectedTheme.id}`);

    if (!response.ok) {
        alert("예약 가능 시간을 불러오지 못했습니다.");
        return;
    }

    const times = await response.json();

    const timeList = document.getElementById("time-list");
    timeList.innerHTML = "";
    selectedTime = null;

    if (times.length === 0) {
        timeList.innerHTML = `<p style="color: #b5b5b5;">예약 가능한 시간이 없습니다.</p>`;
        return;
    }

    times.forEach(time => {
        const button = document.createElement("button");
        button.className = "time-button";
        button.type = "button";
        button.textContent = formatTime(time.startAt);

        button.addEventListener("click", () => {
            document.querySelectorAll(".time-button")
                .forEach(item => item.classList.remove("selected"));

            button.classList.add("selected");
            selectedTime = time;
        });

        timeList.appendChild(button);
    });
}

async function createReservation() {
    const name = document.getElementById("reservation-name-input").value.trim();

    if (!selectedTime) {
        alert("방문 시간을 선택해주세요.");
        return;
    }

    if (!name) {
        alert("예약자 성함을 입력해주세요.");
        return;
    }

    const requestBody = {
        name: name,
        dateId: selectedDate.id,
        timeId: selectedTime.id,
        themeId: selectedTheme.id
    };

    const response = await fetch("/member/reservations", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestBody)
    });

    if (!response.ok) {
        alert("예약에 실패했습니다.");
        return;
    }

    alert("예약이 완료되었습니다.");
    location.href = `/reservation-lookup?name=${encodeURIComponent(name)}`;
}

function formatTime(value) {
    if (!value) {
        return "";
    }

    const parts = value.split(":");
    return `${parts[0]}:${parts[1]}`;
}
