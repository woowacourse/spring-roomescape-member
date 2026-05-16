import {
  createTheme,
  createTime,
  deactivateTime,
  deactivateTheme,
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

  deactivateTheme(id) {
    return deactivateTheme(id);
  }

  addTime(payload) {
    return createTime(payload);
  }

  deactivateTime(id) {
    return deactivateTime(id);
  }
}
