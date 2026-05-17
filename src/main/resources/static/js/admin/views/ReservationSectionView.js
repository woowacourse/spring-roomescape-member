import View from "../../common/View.js";
import { clearElement, formatTime, qs } from "../../common/helpers.js";

export default class ReservationSectionView extends View {
  constructor(element) {
    super(element);
    this.tableBody = qs('[data-role="reservation-table"]', element);
  }

  initializeDate(today = new Date()) {
    return today;
  }

  renderOptions(themes, times) {
    return { themes, times };
  }

  render(reservations) {
    clearElement(this.tableBody);

    if (!reservations.length) {
      this.tableBody.innerHTML = '<tr class="empty-row"><td colspan="6">예약이 없습니다.</td></tr>';
      return;
    }

    reservations.forEach((reservation) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td><span class="badge badge-blue">${reservation.id}</span></td>
        <td class="td-name">${reservation.name}</td>
        <td class="td-name">${reservation.theme.name}</td>
        <td>${reservation.date}</td>
        <td>${formatTime(reservation.time.startAt)}</td>
        <td><span class="badge badge-gray">${reservation.status}</span></td>
      `;
      this.tableBody.appendChild(row);
    });
  }
}
