/**
 * 사용자 예약 조회 페이지 (static/my-reservations.html)
 *
 * API
 *  GET    /reservations/{name}?from=&to=&themeId=  : 이름 기반 예약 조회
 *  PUT    /reservations/{id}                        : 예약 수정 (RESERVED만)
 *  PATCH  /reservations/{id}                        : 예약 취소 (RESERVED만)
 *  GET    /themes                                   : 테마 목록 (필터 select용)
 *  GET    /themes/{themeId}/available-times?date=   : 날짜별 예약 가능 시간
 */
const $ = (sel) => document.querySelector(sel);

async function api(path, options = {}) {
  const res = await fetch(path, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (!res.ok) {
    let msg;
    try { const b = await res.json(); msg = b.message || JSON.stringify(b); }
    catch (_) { msg = `요청 실패 (HTTP ${res.status})`; }
    throw new Error(msg);
  }
  if (res.status === 204) return null;
  return res.json();
}

function escapeHtml(str) {
  return String(str ?? "")
    .replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;").replace(/'/g, "&#39;");
}

function setMessage(msg, isError = false) {
  const el = $("#message");
  el.textContent = msg;
  el.classList.toggle("error", isError);
}

const STATUS_LABEL = { RESERVED: "예약됨", CANCELED: "취소됨", COMPLETED: "이용완료" };

/* ── 결과 렌더링 ── */
function renderResults(reservations) {
  const tbody = $("#resultRows");
  tbody.innerHTML = "";

  if (!reservations.length) {
    tbody.innerHTML = '<tr><td colspan="6" class="muted" style="text-align:center;">조건에 맞는 예약이 없습니다.</td></tr>';
    $("#resultSummary").textContent = "0 건";
    return;
  }

  reservations.forEach((r) => {
    const tr = document.createElement("tr");
    const status = r.status ?? "RESERVED";
    const actionCell = status === "RESERVED"
      ? `<td style="text-align:right;">
           <button class="ghost btn-edit" data-id="${r.id}" data-name="${escapeHtml(r.name ?? "")}"
             data-date="${escapeHtml(r.date)}" data-time-id="${r.time?.id ?? ""}"
             data-time-label="${escapeHtml(r.time?.startAt ?? "")}"
             data-theme-id="${r.theme?.id ?? ""}" data-theme-name="${escapeHtml(r.theme?.name ?? "")}"
             type="button">수정</button>
           <button class="ghost btn-cancel" data-id="${r.id}" type="button"
             style="color:var(--danger); margin-left:6px;">취소</button>
         </td>`
      : `<td></td>`;

    tr.innerHTML = `
      <td class="num">${r.id}</td>
      <td>${escapeHtml(r.date)}</td>
      <td>${escapeHtml(r.time?.startAt ?? "")}</td>
      <td>${escapeHtml(r.theme?.name ?? "")}</td>
      <td><span class="badge badge-${status}">${STATUS_LABEL[status] ?? status}</span></td>
      ${actionCell}
    `;
    tbody.appendChild(tr);
  });

  $("#resultSummary").textContent = `${reservations.length} 건`;
  $("#resultSection").style.display = "";
}

/* ── 조회 ── */
let lastSearchName = "";

async function search() {
  const name = $("#searchName").value.trim();
  if (!name) {
    setMessage("이름을 입력해 주세요.", true);
    return;
  }
  lastSearchName = name;

  const from = $("#searchFrom").value;
  const to = $("#searchTo").value;
  const themeId = $("#searchTheme").value;

  const params = new URLSearchParams();
  if (from) params.append("from", from);
  if (to) params.append("to", to);
  if (themeId) params.append("themeId", themeId);
  const qs = params.toString();

  try {
    const data = await api(`/reservations/${encodeURIComponent(name)}${qs ? "?" + qs : ""}`);
    renderResults(data);
    setMessage(`"${name}" 예약 조회 완료.`);
  } catch (e) {
    setMessage(e.message, true);
  }
}

/* ── 모달: 수정 ── */
// 현재 예약 시간 정보 (날짜 변경 시 "현재 시간" 표시용으로 유지)
let _editPinTimeId    = "";
let _editPinTimeLabel = "";

function openEditModal(btn) {
  _editPinTimeId    = btn.dataset.timeId;
  _editPinTimeLabel = btn.dataset.timeLabel;

  $("#editId").value        = btn.dataset.id;
  $("#editName").value      = btn.dataset.name;
  $("#editDate").value      = btn.dataset.date;
  $("#editThemeId").value   = btn.dataset.themeId;
  $("#editThemeName").value = btn.dataset.themeName;

  // 모달 열릴 때 즉시 예약 가능 시간 조회
  loadEditTimes(btn.dataset.themeId, btn.dataset.date, _editPinTimeId, _editPinTimeLabel);
  $("#editModal").classList.remove("hidden");
}

/**
 * 날짜별 예약 가능 시간 조회 후 #editTime select 채우기.
 * pinTimeId / pinTimeLabel : 현재 예약 시간 (이미 예약된 슬롯이라 available-times 에서 빠지므로 수동 포함)
 *                            날짜를 바꿨을 때는 null 전달 → 핀 없이 가능 시간만 표시
 */
async function loadEditTimes(themeId, date, pinTimeId = null, pinTimeLabel = null) {
  const sel = $("#editTime");
  if (!date || !themeId) {
    sel.innerHTML = '<option value="">날짜를 선택해 주세요</option>';
    return;
  }
  sel.innerHTML = '<option value="">불러오는 중…</option>';
  sel.disabled = true;
  try {
    const times = await api(`/themes/${themeId}/available-times?date=${date}`);
    sel.innerHTML = "";

    // 현재 예약 시간이 available 목록에 없으면 맨 앞에 추가 (이미 본인이 선점한 슬롯)
    if (pinTimeId && !times.some((t) => String(t.id) === String(pinTimeId))) {
      const opt = document.createElement("option");
      opt.value = pinTimeId;
      opt.textContent = `${pinTimeLabel} (현재 예약)`;
      sel.appendChild(opt);
    }

    times.forEach((t) => {
      const opt = document.createElement("option");
      opt.value = t.id;
      opt.textContent = t.startAt;
      sel.appendChild(opt);
    });

    if (!times.length && !pinTimeId) {
      sel.innerHTML = '<option value="">예약 가능한 시간이 없습니다</option>';
    } else {
      sel.value = pinTimeId || (times[0]?.id ?? "");
    }
  } catch (_) {
    sel.innerHTML = '<option value="">시간 로딩 실패</option>';
  } finally {
    sel.disabled = false;
  }
}

async function saveEdit() {
  const id      = $("#editId").value;
  const name    = $("#editName").value.trim();
  const date    = $("#editDate").value;
  const timeId  = Number($("#editTime").value);
  const themeId = Number($("#editThemeId").value);

  if (!name || !date || !timeId || !themeId) {
    setMessage("모든 항목을 입력해 주세요.", true);
    return;
  }

  try {
    await api(`/reservations/${id}`, {
      method: "PUT",
      body: JSON.stringify({ name, date, timeId, themeId }),
    });
    $("#editModal").classList.add("hidden");
    setMessage("예약이 수정되었습니다.");
    search();   // 목록 갱신
  } catch (e) {
    setMessage(e.message, true);
  }
}

async function cancelReservation(id) {
  if (!confirm("예약을 취소하시겠습니까?")) return;
  try {
    await api(`/reservations/${id}`, { method: "PATCH" });
    setMessage("예약이 취소되었습니다.");
    search();   // 목록 갱신
  } catch (e) {
    setMessage(e.message, true);
  }
}

/* ── 이벤트 ── */
$("#resultRows").addEventListener("click", (e) => {
  const editBtn   = e.target.closest(".btn-edit");
  const cancelBtn = e.target.closest(".btn-cancel");
  if (editBtn)   openEditModal(editBtn);
  if (cancelBtn) cancelReservation(cancelBtn.dataset.id);
});

// 날짜 바꾸면 핀 없이 새 가능 시간 목록 조회
$("#editDate").addEventListener("change", () => {
  loadEditTimes($("#editThemeId").value, $("#editDate").value);
});

$("#editSaveBtn").addEventListener("click", saveEdit);
$("#editModalClose").addEventListener("click", () => {
  $("#editModal").classList.add("hidden");
});
$("#editModal").addEventListener("click", (e) => {
  if (e.target === $("#editModal")) $("#editModal").classList.add("hidden");
});

/* ── Init ── */
(async function init() {
  try {
    const themes = await api("/themes");
    const sel = $("#searchTheme");
    themes.forEach((t) => {
      const opt = document.createElement("option");
      opt.value = t.id;
      opt.textContent = t.name;
      sel.appendChild(opt);
    });
  } catch (_) { /* 테마 로딩 실패는 무시 */ }
})();

$("#searchBtn").addEventListener("click", search);
$("#searchName").addEventListener("keydown", (e) => {
  if (e.key === "Enter") search();
});
