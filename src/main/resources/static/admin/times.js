/**
 * 관리자 시간 관리 (templates/admin/times.html)
 *
 * 백엔드 API
 *  - GET    /admin/times        : 전체 시간 조회
 *  - POST   /admin/times        : 시간 추가
 *  - DELETE /admin/times/{id}   : 시간 삭제 (예약에 사용 중이면 409 ResourceInUse)
 */
const $ = (selector) => document.querySelector(selector);

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

function renderTimes(times) {
  const tbody = $("#timeRows");
  tbody.innerHTML = "";

  if (times.length === 0) {
    tbody.innerHTML =
      '<tr><td colspan="3" class="muted" style="text-align: center;">등록된 시간이 없습니다.</td></tr>';
    $("#listSummary").textContent = "0 건";
    return;
  }

  times.forEach((time) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td class="num">${time.id}</td>
      <td>${escapeHtml(time.startAt)}</td>
      <td class="actions"><button class="danger" data-id="${time.id}" type="button">삭제</button></td>
    `;
    tbody.appendChild(tr);
  });
  $("#listSummary").textContent = `${times.length} 건`;
}

async function refresh() {
  const times = await api("/admin/times");
  renderTimes(times);
}

/* ───────── Event wiring ───────── */

$("#timeForm").addEventListener("submit", async (event) => {
  event.preventDefault();
  const form = new FormData(event.target);
  const startAt = form.get("startAt");
  if (!startAt) {
    setMessage("시작 시각을 입력해 주세요.", true);
    return;
  }
  // <input type="time"> → "HH:mm" 형식 → 백엔드 LocalTime 파싱 호환
  try {
    await api("/admin/times", {
      method: "POST",
      body: JSON.stringify({ startAt })
    });
    event.target.reset();
    await refresh();
    setMessage(`시간을 추가했습니다: ${startAt}`);
  } catch (error) {
    setMessage(error.message, true);
  }
});

$("#timeRows").addEventListener("click", async (event) => {
  const button = event.target.closest("button[data-id]");
  if (!button) return;
  const id = button.dataset.id;
  if (!confirm(`#${id} 시간을 삭제하시겠습니까?`)) return;
  try {
    await api(`/admin/times/${id}`, { method: "DELETE" });
    await refresh();
    setMessage(`#${id} 시간을 삭제했습니다.`);
  } catch (error) {
    setMessage(error.message, true);
  }
});

$("#refreshBtn").addEventListener("click", async () => {
  try {
    await refresh();
    setMessage("새로고침 완료.");
  } catch (error) {
    setMessage(error.message, true);
  }
});

/* ───────── Init ───────── */

refresh()
  .then(() => setMessage("초기 로딩 완료."))
  .catch((error) => setMessage(error.message, true));
