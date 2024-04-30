package roomescape.core.domain;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Reservation {
    private Long id;
    private String name;
    private String date;
    private ReservationTime time;

    public Reservation() {
    }

    public Reservation(final String name, final String date, final ReservationTime time) {
        this(null, name, date, time);
    }

    public Reservation(final Long id, final String name, final String date, final ReservationTime time) {
        validateDate(date);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validateDate(final String date) {
        try {
            LocalDate.parse(date);
        } catch (final DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형식이 잘못되었습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public ReservationTime getReservationTime() {
        return time;
    }
}
