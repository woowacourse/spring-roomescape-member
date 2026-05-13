package roomescape.reservation.model;

import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.user.model.User;

public class Reservation {

    private Long id;
    private User user;
    private Schedule schedule;
    private Theme theme;

    protected Reservation() {
    }

    public Reservation(User user, Schedule schedule, Theme theme) {
        this(null, user, schedule, theme);
    }

    public Reservation(Long id, User user, Schedule schedule, Theme theme) {
        validateUser(user);
        validateSchedule(schedule);
        validateTheme(theme);
        this.id = id;
        this.user = user;
        this.schedule = schedule;
        this.theme = theme;
    }

    public boolean isOwnedBy(Long userId) {
        return this.user.getId().equals(userId);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Theme getTheme() {
        return theme;
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보는 필수입니다.");
        }
    }

    private void validateSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("스케줄 정보는 필수입니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마 정보는 필수입니다.");
        }
    }
}
