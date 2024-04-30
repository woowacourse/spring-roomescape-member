package roomescape.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Reservation {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, String name, String date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = toLocalDate(date);
        this.time = time;
    }

    private LocalDate toLocalDate(String date) {
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        }catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜(%s)가 yyyy-MM-dd에 맞지 않습니다.".formatted(date));
        }
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
