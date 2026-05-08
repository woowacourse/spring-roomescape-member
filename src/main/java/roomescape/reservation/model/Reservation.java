package roomescape.reservation.model;

import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.user.model.User;

public class Reservation {

    private Long id;
    private User user;
    private Schedule schedule;

    protected Reservation() {
    }

    public Reservation(User user, Schedule schedule) {
        this(null, user, schedule);
    }

    public Reservation(Long id, User user, Schedule schedule) {
        validateUser(user);
        validateSchedule(schedule);
        this.id = id;
        this.user = user;
        this.schedule = schedule;
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
}
