package roomescape.domain;

import java.time.LocalDate;
import roomescape.exception.InvalidInputException;

public class Reservation {

    private final Long id;
    private final UserName name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, UserName name, LocalDate date, ReservationTime time, Theme theme) {
        validate(date);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(UserName name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    private void validate(LocalDate date) {
        if (date == null) {
            throw new InvalidInputException("예약 날짜가 입력되지 않았습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public UserName getName() {
        return name;
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
