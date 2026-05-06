import View from "../../common/View.js";
import { clearElement, delegate, emit, formatTime, qs } from "../../common/helpers.js";

export default class TimeSectionView extends View {
  constructor(element) {
    super(element);
    this.form = qs('[data-role="time-form"]', element);
    this.tableBody = qs('[data-role="time-table"]', element);
    this.bindEvents();
  }

  bindEvents() {
    this.form.addEventListener("submit", (event) => {
      event.preventDefault();
      const formData = new FormData(this.form);
      emit(this.element, "@create-time", {
        startAt: String(formData.get("startAt") || "")
      });
    });

    delegate(this.tableBody, "click", ".btn-delete", function () {
      emit(this.closest('[data-panel="time"]'), "@delete-time", {
        id: Number(this.dataset.id)
      });
    });
  }

  resetForm() {
    this.form.reset();
  }

  render(times) {
    clearElement(this.tableBody);

    if (!times.length) {
      this.tableBody.innerHTML = '<tr class="empty-row"><td colspan="3">등록된 시간이 없습니다.</td></tr>';
      return;
    }

    times.forEach((time) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td><span class="badge badge-gray">${time.id}</span></td>
        <td class="td-name">${formatTime(time.startAt)}</td>
        <td><button class="btn-delete" type="button" data-id="${time.id}">삭제</button></td>
      `;
      this.tableBody.appendChild(row);
    });
  }
}
