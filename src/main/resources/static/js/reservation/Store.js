import { getSearchParam } from "../common/helpers.js";
import {
  cancelReservation,
  createReservation,
  fetchReservationsByName,
  fetchThemes,
  fetchThemeSlots,
  modifyReservation
} from "./api.js";

export default class Store {
  constructor() {
    this.themes = [];
    this.slots = [];
    this.reservations = [];
    this.selectedThemeId = getSearchParam("themeId") || "";
    this.selectedDate = "";
    this.selectedTimeId = null;
    this.name = "";
    this.lookupName = "";
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

  setLookupName(name) {
    this.lookupName = name.trim();
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

  async loadReservationsByName() {
    if (!this.lookupName) {
      this.reservations = [];
      return;
    }

    this.reservations = await fetchReservationsByName(this.lookupName);
  }

  loadSlotsFor(themeId, date) {
    return fetchThemeSlots(themeId, date);
  }

  cancelReservation(id, name) {
    return cancelReservation(id, { name });
  }

  modifyReservation(id, payload) {
    return modifyReservation(id, payload);
  }
}
