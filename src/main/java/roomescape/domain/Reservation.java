package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final User user;
    private final Theme theme;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, User user, Theme theme, LocalDate date, ReservationTime time) {
        if (user == null) {
            throw new IllegalArgumentException("사용자는 필수입니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
        this.id = id;
        this.user = user;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
