import { getSearchParam } from "../common/helpers.js";
import { createReservation, fetchThemes, fetchThemeSlots } from "./api.js";

export default class Store {
  constructor() {
    this.themes = [];
    this.slots = [];
    this.selectedThemeId = getSearchParam("themeId") || "";
    this.selectedDate = "";
    this.selectedTimeId = null;
    this.name = "";
  }

  async loadThemes() {
    this.themes = await fetchThemes();
  }

  async loadSlots() {
    if (!this.selectedThemeId || !this.selectedDate) {
      this.slots = [];
      this.selectedTimeId = null;
      return;
    }

    this.slots = await fetchThemeSlots(this.selectedThemeId, this.selectedDate);
    this.selectedTimeId = null;
  }

  setThemeId(themeId) {
    this.selectedThemeId = themeId;
  }

  setDate(date) {
    this.selectedDate = date;
  }

  setName(name) {
    this.name = name.trim();
  }

  setSelectedTimeId(timeId) {
    this.selectedTimeId = timeId;
  }

  canSubmit() {
    return Boolean(this.selectedThemeId && this.selectedDate && this.name && this.selectedTimeId);
  }

  submit() {
    return createReservation({
      name: this.name,
      date: this.selectedDate,
      themeId: Number(this.selectedThemeId),
      timeId: this.selectedTimeId
    });
  }
}
