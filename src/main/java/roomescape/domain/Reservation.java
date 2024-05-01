package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Reservation {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, String date, ReservationTime time, Theme theme) {
        this(Objects.requireNonNull(id, "인자 중 null 값이 존재합니다."), name, toLocalDate(date), time, theme);
    }

    public Reservation(String name, String date, ReservationTime time, Theme theme) {
        this(null, name, toLocalDate(date), time, theme);
    }

    private Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        final String errorMessage = "인자 중 null 값이 존재합니다.";
        this.id = id;
        this.name = Objects.requireNonNull(name, errorMessage);
        this.date = Objects.requireNonNull(date, errorMessage);
        this.time = Objects.requireNonNull(time, errorMessage);
        this.theme = Objects.requireNonNull(theme, errorMessage);
    }

    private static LocalDate toLocalDate(String date) {
        Objects.requireNonNull(date, "인자 중 null 값이 존재합니다.");
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜(%s)가 yyyy-MM-dd에 맞지 않습니다.".formatted(date));
        }
    }

    public boolean isBefore(LocalDateTime currentDateTime) {
        LocalDate currentDate = currentDateTime.toLocalDate();
        if (date.isBefore(currentDate)) {
            return true;
        }
        if (date.isAfter(currentDate)) {
            return false;
        }
        return time.isBefore(currentDateTime.toLocalTime());
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

    public Theme getTheme() {
        return theme;
    }
}
