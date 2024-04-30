package roomescape.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation {

    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(final Long id, final String name, final String date, final ReservationTime time) {
        this(id, name, parseDate(date), time);
    }

    public Reservation(final String name, final String date, final ReservationTime time) {
        this(null, name, parseDate(date), time);
    }

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time) {
        this.id = id;
        this.name = new Name(name);
        this.date = date;
        this.time = time;
    }

    private static LocalDate parseDate(final String date) {
        if (date.isEmpty()) {
            throw new IllegalArgumentException("날짜가 입력되지 않았습니다.");
        }
        return LocalDate.parse(date);
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
}
