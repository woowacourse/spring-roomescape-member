package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    //TODO: 정적 팩토리 메서드로 변경
    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time,
                       final Theme theme) {
        validateNameLength(name);
        validateDateTime(date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final String name, final LocalDate date, final ReservationTime time, final Theme theme) {
        validateNameLength(name);
        validateDateTime(date, time);
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateNameLength(final String name) {
        if (name.length() > 10) {
            throw new IllegalArgumentException("예약자명은 10자 이하여야합니다.");
        }
    }

    private void validateDateTime(final LocalDate localDate, final ReservationTime reservationTime) {
        if (localDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("예약은 미래만 가능합니다.");
        }
        if (localDate.isEqual(LocalDate.now()) && reservationTime.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("예약은 미래만 가능합니다.");
        }
    }

    public boolean isEqualId(final Long id) {
        return this.id.equals(id);
    }

    public Long getId() {
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
