export default class Controller {
  constructor(store, views) {
    this.store = store;
    this.views = views;
    this.subscribeViewEvents();
  }

  subscribeViewEvents() {
    this.views.formView
      .on("@themechange", (event) => this.handleThemeChange(event.detail.themeId))
      .on("@datechange", (event) => this.handleDateChange(event.detail.date))
      .on("@namechange", (event) => this.handleNameChange(event.detail.name))
      .on("@submit", () => this.submit());

    this.views.slotGridView.on("@slotselect", (event) => {
      this.store.setSelectedTimeId(event.detail.timeId);
      this.render();
    });
  }

  async initialize() {
    const initialDate = this.views.formView.initializeDate();
    this.store.setDate(initialDate);
    this.views.slotGridView.showIdle();

    try {
      await this.store.loadThemes();
      this.views.formView.renderThemes(this.store.themes, this.store.selectedThemeId);

      if (this.store.selectedThemeId) {
        await this.loadSlots();
      } else {
        this.render();
      }
    } catch (error) {
      this.views.toastView.show(error.message, true);
    }
  }

  async handleThemeChange(themeId) {
    this.store.setThemeId(themeId);
    await this.loadSlots();
  }

  async handleDateChange(date) {
    this.store.setDate(date);
    await this.loadSlots();
  }

  handleNameChange(name) {
    this.store.setName(name);
    this.render();
  }

  async loadSlots() {
    if (!this.store.selectedThemeId || !this.store.selectedDate) {
      this.store.slots = [];
      this.store.setSelectedTimeId(null);
      this.views.slotGridView.showIdle();
      this.render();
      return;
    }

    this.views.slotGridView.showLoading();

    try {
      await this.store.loadSlots();
      this.render();
    } catch (error) {
      this.views.slotGridView.showError();
      this.views.toastView.show(error.message, true);
      this.views.formView.sync({
        selectedThemeId: this.store.selectedThemeId,
        selectedDate: this.store.selectedDate,
        name: this.store.name,
        canSubmit: this.store.canSubmit()
      });
    }
  }

  async submit() {
    try {
      await this.store.submit();
      window.location.href = "/";
    } catch (error) {
      this.views.toastView.show(error.message || "예약에 실패했습니다.", true);
    }
  }

  render() {
    this.views.formView.sync({
      selectedThemeId: this.store.selectedThemeId,
      selectedDate: this.store.selectedDate,
      name: this.store.name,
      canSubmit: this.store.canSubmit()
    });

    if (this.store.selectedThemeId && this.store.selectedDate) {
      this.views.slotGridView.render(this.store.slots, this.store.selectedTimeId);
    }
  }
}
