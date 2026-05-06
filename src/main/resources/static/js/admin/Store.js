import {
  createReservation,
  createTheme,
  createTime,
  deleteReservation,
  deleteTheme,
  deleteTime,
  fetchReservations,
  fetchThemes,
  fetchTimes
} from "./api.js";

export const TabType = {
  THEME: "theme",
  RESERVATION: "reservation",
  TIME: "time"
};

export default class Store {
  constructor() {
    this.selectedTab = TabType.THEME;
    this.themes = [];
    this.reservations = [];
    this.times = [];
  }

  selectTab(tab) {
    this.selectedTab = tab;
  }

  async loadAll() {
    const [themes, reservations, times] = await Promise.all([
      fetchThemes(),
      fetchReservations(),
      fetchTimes()
    ]);

    this.themes = themes;
    this.reservations = reservations;
    this.times = times;
  }

  addTheme(payload) {
    return createTheme(payload);
  }

  removeTheme(id) {
    return deleteTheme(id);
  }

  addReservation(payload) {
    return createReservation(payload);
  }

  removeReservation(id) {
    return deleteReservation(id);
  }

  addTime(payload) {
    return createTime(payload);
  }

  removeTime(id) {
    return deleteTime(id);
  }
}
