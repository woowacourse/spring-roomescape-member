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
      .on("@deactivate-theme", (event) => this.deactivateTheme(event.detail.id));

    this.views.timeSectionView
      .on("@create-time", (event) => this.createTime(event.detail))
      .on("@deactivate-time", (event) => this.deactivateTime(event.detail.id));
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

  async deactivateTheme(id) {
    try {
      await this.store.deactivateTheme(id);
      this.views.toastView.show("테마가 비활성화되었습니다.");
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

  async deactivateTime(id) {
    try {
      await this.store.deactivateTime(id);
      this.views.toastView.show("시간이 비활성화되었습니다.");
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
