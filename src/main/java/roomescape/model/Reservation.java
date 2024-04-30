package roomescape.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation {

    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(final String name, final LocalDate date, final ReservationTime time) {
        this(null, name, date, time);
    }

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time) {
        validateDate(date);
        this.id = id;
        this.name = new Name(name);
        this.date = date;
        this.time = time;
    }

    private static void validateDate(final LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("날짜가 비어 있습니다.");
        }
    }


    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }
}
