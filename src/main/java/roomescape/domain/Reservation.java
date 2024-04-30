package roomescape.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, String name, String date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = LocalDate.parse(date, DATE_FORMATTER);
        this.time = time;
    }

    private Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation changeId(Long id) {
        return new Reservation(id, this.name, this.date, this.time);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return DATE_FORMATTER.format(date);
    }

    public ReservationTime getTime() {
        return time;
    }
}
