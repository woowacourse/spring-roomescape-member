import View from "../../common/View.js";
import { clearElement, delegate, emit, formatDateInputValue, formatTime, qs } from "../../common/helpers.js";

export default class UserReservationListView extends View {
  constructor(element) {
    super(element);
    this.form = qs('[data-role="reservation-search-form"]', element);
    this.nameInput = qs("#reservationSearchName", element);
    this.tableBody = qs('[data-role="user-reservation-table"]', element);
    this.bindEvents();
  }

  bindEvents() {
    this.form.addEventListener("submit", (event) => {
      event.preventDefault();
      emit(this.element, "@lookup-reservations", {
        name: this.nameInput.value.trim()
      });
    });

    delegate(this.tableBody, "click", ".btn-load-slots", function () {
      const row = this.closest("tr");
      emit(this.closest('[data-role="user-reservations"]'), "@load-modify-slots", {
        id: Number(row.dataset.id),
        themeId: Number(row.dataset.themeId),
        date: qs(".modify-date", row).value
      });
    });

    delegate(this.tableBody, "input", ".modify-date", this.emitModifyDateChange);
    delegate(this.tableBody, "change", ".modify-date", this.emitModifyDateChange);

    delegate(this.tableBody, "click", ".btn-modify", function () {
      const row = this.closest("tr");
      emit(this.closest('[data-role="user-reservations"]'), "@modify-reservation", {
        id: Number(row.dataset.id),
        name: row.dataset.name,
        date: qs(".modify-date", row).value,
        timeId: Number(qs(".modify-time", row).value)
      });
    });

    delegate(this.tableBody, "click", ".btn-cancel", function () {
      const row = this.closest("tr");
      emit(this.closest('[data-role="user-reservations"]'), "@cancel-reservation", {
        id: Number(row.dataset.id),
        name: row.dataset.name
      });
    });
  }

  emitModifyDateChange() {
    const row = this.closest("tr");
    const timeSelect = qs(".modify-time", row);
    timeSelect.innerHTML = '<option value="">시간을 다시 조회하세요</option>';
    emit(this.closest('[data-role="user-reservations"]'), "@load-modify-slots", {
      id: Number(row.dataset.id),
      themeId: Number(row.dataset.themeId),
      date: this.value
    });
  }

  setLookupName(name) {
    this.nameInput.value = name;
  }

  render(reservations) {
    clearElement(this.tableBody);

    if (!reservations.length) {
      this.tableBody.innerHTML = '<tr class="empty-row"><td colspan="7">조회된 예약이 없습니다.</td></tr>';
      return;
    }

    reservations.forEach((reservation) => {
      const row = document.createElement("tr");
      const isReserved = reservation.status === "RESERVED";
      row.dataset.id = reservation.id;
      row.dataset.name = reservation.name;
      row.dataset.themeId = reservation.theme.id;
      row.innerHTML = `
        <td>${reservation.name}</td>
        <td>${reservation.theme.name}</td>
        <td>${reservation.date}</td>
        <td>${formatTime(reservation.time.startAt)}</td>
        <td><span class="status-badge">${reservation.status}</span></td>
        <td>
          ${isReserved ? `
            <div class="modify-controls">
              <input class="modify-date" type="date" value="${reservation.date}" min="${formatDateInputValue()}" />
              <button class="btn-secondary btn-load-slots" type="button">시간 조회</button>
              <select class="modify-time">
                <option value="">시간 조회 필요</option>
              </select>
              <button class="btn-secondary btn-modify" type="button">수정</button>
            </div>
          ` : "-"}
        </td>
        <td>${isReserved ? '<button class="btn-danger btn-cancel" type="button">취소</button>' : "-"}</td>
      `;
      this.tableBody.appendChild(row);
    });
  }

  renderModifySlots(reservationId, slots) {
    const row = this.tableBody.querySelector(`tr[data-id="${reservationId}"]`);

    if (!row) {
      return;
    }

    const select = qs(".modify-time", row);
    select.innerHTML = "";

    slots.filter((slot) => slot.isReservable).forEach((slot) => {
      const option = document.createElement("option");
      option.value = slot.id;
      option.textContent = formatTime(slot.startAt);
      select.appendChild(option);
    });

    if (!select.children.length) {
      select.innerHTML = '<option value="">예약 가능한 시간 없음</option>';
    }
  }
}
