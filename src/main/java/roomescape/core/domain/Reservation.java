package roomescape.core.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final String name, final String date, final ReservationTime time, final Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(final Long id, final String name, final String date, final ReservationTime time,
                       final Theme theme) {
        this.id = id;
        this.name = name;
        this.date = parseDate(date);
        this.time = time;
        this.theme = theme;
    }

    private LocalDate parseDate(final String date) {
        try {
            return LocalDate.parse(date);
        } catch (final DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형식이 잘못되었습니다.");
        }
    }

    public boolean isDatePast() {
        return date.isBefore(LocalDate.now());
    }

    public boolean isDateToday() {
        return date.isEqual(LocalDate.now());
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

    public String getDateString() {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public Long getTimeId() {
        return time.getId();
    }

    public ReservationTime getReservationTime() {
        return time;
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public Theme getTheme() {
        return theme;
    }
}
