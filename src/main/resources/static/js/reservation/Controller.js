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

    this.views.userReservationListView
      .on("@lookup-reservations", (event) => this.lookupReservations(event.detail.name))
      .on("@load-modify-slots", (event) => this.loadModifySlots(event.detail))
      .on("@modify-reservation", (event) => this.modifyReservation(event.detail))
      .on("@cancel-reservation", (event) => this.cancelReservation(event.detail));
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
      this.views.toastView.show("예약이 완료되었습니다.");
      await this.lookupReservations(this.store.name);
    } catch (error) {
      this.views.toastView.show(error.message || "예약에 실패했습니다.", true);
    }
  }

  async lookupReservations(name) {
    this.store.setLookupName(name);

    if (!this.store.lookupName) {
      this.views.toastView.show("예약자 이름을 입력하세요.", true);
      return;
    }

    try {
      await this.store.loadReservationsByName();
      this.views.userReservationListView.setLookupName(this.store.lookupName);
      this.render();
    } catch (error) {
      this.views.toastView.show(error.message || "예약 조회에 실패했습니다.", true);
    }
  }

  async loadModifySlots({ id, themeId, date }) {
    if (!date) {
      this.views.toastView.show("수정할 날짜를 선택하세요.", true);
      return;
    }

    try {
      const slots = await this.store.loadSlotsFor(themeId, date);
      this.views.userReservationListView.renderModifySlots(id, slots);
    } catch (error) {
      this.views.toastView.show(error.message || "시간 조회에 실패했습니다.", true);
    }
  }

  async modifyReservation({ id, name, date, timeId }) {
    if (!date || !timeId) {
      this.views.toastView.show("수정할 날짜와 시간을 선택하세요.", true);
      return;
    }

    try {
      await this.store.modifyReservation(id, { name, date, timeId });
      this.views.toastView.show("예약이 수정되었습니다.");
      await this.lookupReservations(name);
    } catch (error) {
      this.views.toastView.show(error.message || "예약 수정에 실패했습니다.", true);
    }
  }

  async cancelReservation({ id, name }) {
    try {
      await this.store.cancelReservation(id, name);
      this.views.toastView.show("예약이 취소되었습니다.");
      await this.lookupReservations(name);
    } catch (error) {
      this.views.toastView.show(error.message || "예약 취소에 실패했습니다.", true);
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

    this.views.userReservationListView.render(this.store.reservations);
  }
}
