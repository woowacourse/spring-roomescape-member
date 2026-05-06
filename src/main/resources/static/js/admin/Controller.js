import { TabType } from "./Store.js";

export default class Controller {
  constructor(store, views) {
    this.store = store;
    this.views = views;
    this.subscribeViewEvents();
  }

  subscribeViewEvents() {
    this.views.tabView.on("@tabchange", (event) => {
      this.store.selectTab(event.detail.tab);
      this.views.tabView.render(this.store.selectedTab);
    });

    this.views.themeSectionView
      .on("@create-theme", (event) => this.createTheme(event.detail))
      .on("@delete-theme", (event) => this.deleteTheme(event.detail.id));

    this.views.reservationSectionView
      .on("@create-reservation", (event) => this.createReservation(event.detail))
      .on("@delete-reservation", (event) => this.deleteReservation(event.detail.id));

    this.views.timeSectionView
      .on("@create-time", (event) => this.createTime(event.detail))
      .on("@delete-time", (event) => this.deleteTime(event.detail.id));
  }

  async initialize() {
    this.views.tabView.render(TabType.THEME);
    this.views.reservationSectionView.initializeDate();
    await this.refreshAll();
  }

  async refreshAll() {
    try {
      await this.store.loadAll();
      this.render();
    } catch (error) {
      this.views.toastView.show(error.message, "error");
    }
  }

  async createTheme(payload) {
    if (!payload.name || !payload.description || !payload.thumbnailImageUrl) {
      this.views.toastView.show("모든 항목을 입력하세요.", "error");
      return;
    }

    try {
      await this.store.addTheme(payload);
      this.views.themeSectionView.resetForm();
      this.views.toastView.show("테마가 추가되었습니다.", "success");
      await this.refreshAll();
    } catch (error) {
      this.views.toastView.show(error.message, "error");
    }
  }

  async deleteTheme(id) {
    try {
      await this.store.removeTheme(id);
      this.views.toastView.show("테마가 삭제되었습니다.");
      await this.refreshAll();
    } catch (error) {
      this.views.toastView.show(error.message, "error");
    }
  }

  async createReservation(payload) {
    if (!payload.name || !payload.date || !payload.themeId || !payload.timeId) {
      this.views.toastView.show("모든 항목을 입력하세요.", "error");
      return;
    }

    try {
      await this.store.addReservation(payload);
      this.views.reservationSectionView.resetForm();
      this.views.toastView.show("예약이 추가되었습니다.", "success");
      await this.refreshAll();
    } catch (error) {
      this.views.toastView.show(error.message, "error");
    }
  }

  async deleteReservation(id) {
    try {
      await this.store.removeReservation(id);
      this.views.toastView.show("예약이 취소되었습니다.");
      await this.refreshAll();
    } catch (error) {
      this.views.toastView.show(error.message, "error");
    }
  }

  async createTime(payload) {
    if (!payload.startAt) {
      this.views.toastView.show("시간을 입력하세요.", "error");
      return;
    }

    try {
      await this.store.addTime(payload);
      this.views.timeSectionView.resetForm();
      this.views.toastView.show("시간이 추가되었습니다.", "success");
      await this.refreshAll();
    } catch (error) {
      this.views.toastView.show(error.message, "error");
    }
  }

  async deleteTime(id) {
    try {
      await this.store.removeTime(id);
      this.views.toastView.show("시간이 삭제되었습니다.");
      await this.refreshAll();
    } catch (error) {
      this.views.toastView.show(error.message, "error");
    }
  }

  render() {
    this.views.tabView.render(this.store.selectedTab);
    this.views.themeSectionView.render(this.store.themes);
    this.views.reservationSectionView.renderOptions(this.store.themes, this.store.times);
    this.views.reservationSectionView.render(this.store.reservations);
    this.views.timeSectionView.render(this.store.times);
  }
}
