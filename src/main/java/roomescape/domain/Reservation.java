package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final User user;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, User user, LocalDate date, ReservationTime time, Theme theme) {
        validate(date);
        this.id = id;
        this.user = user;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(User user, LocalDate date, ReservationTime time, Theme theme) {
        this(null, user, date, time, theme);
    }

    private void validate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜가 입력되지 않았습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
