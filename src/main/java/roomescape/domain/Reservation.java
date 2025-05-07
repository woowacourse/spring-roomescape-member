package roomescape.domain;

import roomescape.exception.reservation.ReservationFieldRequiredException;

public class Reservation {
    private final Long id;
    private final String name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(Long id, String name, ReservationDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createWithoutId(String name, ReservationDate date, ReservationTime time, Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }

    public static Reservation createWithId(Long id, String name, ReservationDate date, ReservationTime time,
                                           Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    private void validate(String name, ReservationDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new ReservationFieldRequiredException("이름");
        }
    }

    private void validateDate(ReservationDate date) {
        if (date == null) {
            throw new ReservationFieldRequiredException("날짜");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new ReservationFieldRequiredException("시간");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new ReservationFieldRequiredException("테마");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
