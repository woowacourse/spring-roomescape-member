import View from "../../common/View.js";
import {clearElement, delegate, emit, qs} from "../../common/helpers.js";

export default class ThemeSectionView extends View {
  constructor(element) {
    super(element);
    this.form = qs('[data-role="theme-form"]', element);
    this.tableBody = qs('[data-role="theme-table"]', element);
    this.bindEvents();
  }

  bindEvents() {
    this.form.addEventListener("submit", (event) => {
      event.preventDefault();
      const formData = new FormData(this.form);
      emit(this.element, "@create-theme", {
        name: String(formData.get("name") || "").trim(),
        description: String(formData.get("description") || "").trim(),
        thumbnailImageUrl: String(formData.get("thumbnailImageUrl") || "").trim()
      });
    });

    delegate(this.tableBody, "click", ".btn-delete", (event) => {
      const button = event.target.closest(".btn-delete");
      emit(this.element, "@delete-theme", {
        id: Number(button.dataset.id)
      });
    });
  }

  resetForm() {
    this.form.reset();
  }

  render(themes) {
    clearElement(this.tableBody);

    if (!themes.length) {
      this.tableBody.innerHTML = '<tr class="empty-row"><td colspan="4">등록된 테마가 없습니다.</td></tr>';
      return;
    }

    themes.forEach((theme) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td><span class="badge badge-gray">${theme.id}</span></td>
        <td class="td-name">${theme.name}</td>
        <td class="td-description">${theme.description}</td>
        <td><button class="btn-delete" type="button" data-id="${theme.id}">삭제</button></td>
      `;
      this.tableBody.appendChild(row);
    });
  }
}
