import View from "../../common/View.js";
import {clearElement, createElement, delegate} from "../../common/helpers.js";

export default class SearchResultView extends View {
    constructor(element) {
        super(element);
        this.bindEvents();
    }

    bindEvents() {
        delegate(this.element, "click", ".btn-change", (event) => {
            const id = event.target.closest(".reservation-item").dataset.id;
            this.emit("@change", { id });
        });

        delegate(this.element, "click", ".btn-cancel", (event) => {
            const id = event.target.closest(".reservation-item").dataset.id;
            this.emit("@cancel", { id });
        });
    }

    render(page) {
        clearElement(this.element);

        if (!page || !page.content.length) {
            this.element.innerHTML = `
        <div class="empty-state">예약 내역을 검색하세요</div>
      `;
            return;
        }

        page.content.forEach((reservation) => {
            const item = createElement("div", "reservation-item");

            item.dataset.id = reservation.id;
            item.innerHTML = `
        <div>
          <div class="res-theme">${reservation.theme}</div>
          <div class="res-details">
            ${reservation.date} · ${reservation.startAt.slice(0, 5)} · ${reservation.name}
          </div>
        </div>

        <div class="res-actions">
          <button class="btn-change">변경</button>
          <button class="btn-cancel">취소</button>
        </div>
      `;

            this.element.appendChild(item);
        });
    }
}