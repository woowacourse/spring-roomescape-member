package roomescape.core.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Reservation {
    private static final Long DEFAULT_ID = 0L;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final String name, final String date, final ReservationTime time, final Theme theme) {
        this(DEFAULT_ID, name, date, time, theme);
    }

    public Reservation(final Long id, final String name, final String date, final ReservationTime time,
                       final Theme theme) {
        validateEmpty(id, name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = parseDate(date);
        this.time = time;
        this.theme = theme;
    }

    private void validateEmpty(final Long id, final String name, final String date, final ReservationTime time,
                               final Theme theme) {
        if (id == null) {
            throw new IllegalArgumentException("예약 id는 null일 수 없습니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약 이름은 null이나 빈 값일 수 없습니다.");
        }
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("예약 날짜는 null이나 빈 값일 수 없습니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("예약 시간은 null일 수 없습니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("예약 테마는 null일 수 없습니다.");
        }
    }

    private LocalDate parseDate(final String date) {
        try {
            return LocalDate.parse(date);
        } catch (final DateTimeParseException e) {
            throw new IllegalArgumentException("예약 날짜 형식이 잘못되었습니다.");
        }
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

    public boolean isDatePast() {
        return date.isBefore(LocalDate.now());
    }

    public boolean isDateToday() {
        return date.isEqual(LocalDate.now());
    }
}
