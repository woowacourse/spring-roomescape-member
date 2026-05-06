export const ADMIN_TABS = new Set(["themes", "times", "reservations"]);
export const ROUTES = new Set(["home", "user", "admin"]);

export const state = {
  role: null,
  adminTab: "themes",
  themes: [],
  popularThemes: [],
  availableTimes: [],
  adminTimes: [],
  reservations: [],
  selectedThemeId: null,
  selectedDate: tomorrowString(),
  selectedTimeId: null,
  reservationName: "",
  themeDraft: {
    name: "",
    description: "",
    thumbnailImgUrl: ""
  },
  timeDraft: {
    startAt: ""
  },
  loading: {
    user: false,
    times: false,
    adminThemes: false,
    adminTimes: false,
    reservations: false
  },
  submitting: false,
  confirm: null
};

export const indicatorRects = {
  role: null,
  admin: null,
  theme: null,
  time: null
};

export function selectedThemeEntity() {
  return state.themes.find((theme) => Number(theme.id) === Number(state.selectedThemeId));
}

export function todayString() {
  return toDateInputValue(new Date());
}

export function tomorrowString() {
  const date = new Date();
  date.setDate(date.getDate() + 1);
  return toDateInputValue(date);
}

export function toDateInputValue(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}
