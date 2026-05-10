/**
 * 관리자 예약 관리 (templates/admin/reservations.html)
 *
 * 백엔드 API
 *  - GET    /admin/reservations?date=&themeId=  : 필터 조회
 *  - POST   /admin/reservations                 : 예약 추가
 *  - DELETE /admin/reservations/{id}            : 단건 삭제
 *  - DELETE /admin/reservations                 : 전체 삭제
 *  - GET    /admin/times                        : 시간 슬롯 (선택용)
 *  - GET    /themes                             : 테마 목록 (선택용)
 */
const $ = (selector) => document.querySelector(selector);

const state = {
  themes: [],
  times: []
};

async function api(path, options = {}) {
  const response = await fetch(path, {
    headers: { "Content-Type": "application/json" },
    ...options
  });
  if (!response.ok) {
    let message;
    try {
      const body = await response.json();
      message = body.message || JSON.stringify(body);
    } catch (_) {
      message = (await response.text()) || `요청 실패 (HTTP ${response.status})`;
    }
    throw new Error(message);
  }
  if (response.status === 204) return null;
  return response.json();
}

function setMessage(message, isError = false) {
  const el = $("#message");
  el.textContent = message;
  el.classList.toggle("error", isError);
}

function escapeHtml(str) {
  return String(str ?? "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

/* ───────── Selectors ───────── */

function fillThemeSelect(selectEl, includeAllOption) {
  const current = selectEl.value;
  selectEl.innerHTML = includeAllOption
    ? '<option value="">전체 테마</option>'
    : '<option value="">테마 선택</option>';
  state.themes.forEach((theme) => {
    const option = document.createElement("option");
    option.value = theme.id;
    option.textContent = `#${theme.id} ${theme.name}`;
    selectEl.appendChild(option);
  });
  if (current) selectEl.value = current;
}

function fillTimeSelect(selectEl) {
  const current = selectEl.value;
  selectEl.innerHTML = '<option value="">시간 선택</option>';
  state.times.forEach((time) => {
    const option = document.createElement("option");
    option.value = time.id;
    option.textContent = `#${time.id} ${time.startAt}`;
    selectEl.appendChild(option);
  });
  if (current) selectEl.value = current;
}

async function loadSelectors() {
  const [themes, times] = await Promise.all([
    api("/themes"),
    api("/admin/times")
  ]);
  state.themes = themes;
  state.times = times;

  fillThemeSelect($("#filterTheme"), true);
  fillThemeSelect(document.querySelector('#createForm select[name="themeId"]'), false);
  fillTimeSelect(document.querySelector('#createForm select[name="timeId"]'));
}

/* ───────── List ───────── */

function renderReservations(reservations) {
  const tbody = $("#reservationRows");
  tbody.innerHTML = "";

  if (reservations.length === 0) {
    tbody.innerHTML =
      '<tr><td colspan="6" class="muted" style="text-align: center;">조건에 맞는 예약이 없습니다.</td></tr>';
    $("#listSummary").textContent = "0 건";
    return;
  }

  reservations.forEach((r) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td class="num">${r.id}</td>
      <td>${escapeHtml(r.name)}</td>
      <td>${escapeHtml(r.date)}</td>
      <td>${escapeHtml(r.time?.startAt ?? "")}</td>
      <td>${escapeHtml(r.theme?.name ?? "")}</td>
      <td class="actions"><button class="danger" data-id="${r.id}" type="button">삭제</button></td>
    `;
    tbody.appendChild(tr);
  });
  $("#listSummary").textContent = `${reservations.length} 건`;
}

function buildQuery() {
  const date = $("#filterDate").value;
  const themeId = $("#filterTheme").value;
  const params = new URLSearchParams();
  if (date) params.append("date", date);
  if (themeId) params.append("themeId", themeId);
  const qs = params.toString();
  return qs ? `?${qs}` : "";
}

async function refreshList() {
  const reservations = await api(`/admin/reservations${buildQuery()}`);
  renderReservations(reservations);
}

/* ───────── Event wiring ───────── */

$("#applyFilter").addEventListener("click", async () => {
  try {
    await refreshList();
    setMessage("필터 조건으로 조회했습니다.");
  } catch (error) {
    setMessage(error.message, true);
  }
});

$("#resetFilter").addEventListener("click", async () => {
  $("#filterDate").value = "";
  $("#filterTheme").value = "";
  try {
    await refreshList();
    setMessage("필터를 해제하고 전체 조회했습니다.");
  } catch (error) {
    setMessage(error.message, true);
  }
});

$("#refreshList").addEventListener("click", async () => {
  try {
    await refreshList();
    setMessage("새로고침 완료.");
  } catch (error) {
    setMessage(error.message, true);
  }
});

$("#createBtn").addEventListener("click", async () => {
  const form = $("#createForm");
  const data = new FormData(form);
  const payload = {
    name: (data.get("name") || "").trim(),
    date: data.get("date"),
    timeId: data.get("timeId") ? Number(data.get("timeId")) : null,
    themeId: data.get("themeId") ? Number(data.get("themeId")) : null
  };

  if (!payload.name || !payload.date || !payload.timeId || !payload.themeId) {
    setMessage("이름/날짜/시간/테마를 모두 입력해 주세요.", true);
    return;
  }

  try {
    const created = await api("/admin/reservations", {
      method: "POST",
      body: JSON.stringify(payload)
    });
    form.reset();
    await refreshList();
    setMessage(`예약 추가 완료 · #${created.id} ${created.date} ${created.time?.startAt ?? ""} / ${created.name}`);
  } catch (error) {
    setMessage(error.message, true);
  }
});

$("#reservationRows").addEventListener("click", async (event) => {
  const button = event.target.closest("button[data-id]");
  if (!button) return;
  const id = button.dataset.id;
  if (!confirm(`#${id} 예약을 삭제하시겠습니까?`)) return;
  try {
    await api(`/admin/reservations/${id}`, { method: "DELETE" });
    await refreshList();
    setMessage(`#${id} 예약을 삭제했습니다.`);
  } catch (error) {
    setMessage(error.message, true);
  }
});

$("#deleteAll").addEventListener("click", async () => {
  if (!confirm("정말 모든 예약을 삭제할까요? 이 작업은 되돌릴 수 없습니다.")) return;
  try {
    await api("/admin/reservations", { method: "DELETE" });
    await refreshList();
    setMessage("모든 예약을 삭제했습니다.");
  } catch (error) {
    setMessage(error.message, true);
  }
});

/* ───────── Init ───────── */

(async function init() {
  try {
    await loadSelectors();
    await refreshList();
    setMessage("초기 로딩 완료.");
  } catch (error) {
    setMessage(error.message, true);
  }
})();
