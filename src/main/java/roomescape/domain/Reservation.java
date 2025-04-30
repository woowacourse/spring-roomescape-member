package roomescape.domain;

import java.time.LocalDate;
import roomescape.exception.custom.InvalidInputException;

public class Reservation {

    private final long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final long id,
                       final String name,
                       final LocalDate date,
                       final ReservationTime time,
                       final Theme theme) {
        validate(name, date, time, theme);

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final String name,
                       final LocalDate date,
                       final ReservationTime reservationTime,
                       final Theme theme) {
        this(0, name, date, reservationTime, theme);
    }

    private void validate(final String name, final LocalDate date,
                          final ReservationTime reservationTime, final Theme theme) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("예약자 명은 빈 값이 입력될 수 없습니다");
        }
        if (date == null) {
            throw new InvalidInputException("예약 날짜는 빈 값이 입력될 수 없습니다");
        }
        if (reservationTime == null) {
            throw new InvalidInputException("예약 시간은 빈 값이 입력될 수 없습니다");
        }
        if (theme == null) {
            throw new InvalidInputException("테마는 빈 값이 입력될 수 없습니다");
        }
    }

    public Reservation withId(final long id) {
        return new Reservation(id, this.name, this.date, this.time, this.theme);
    }

    public boolean isSameDateTime(final LocalDate date, final long timeId) {
        return this.date.equals(date) && this.time.getId() == timeId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
