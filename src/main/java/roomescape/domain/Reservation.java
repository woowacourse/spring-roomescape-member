package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        final String errorMessage = "인자 중 null 값이 존재합니다.";
        this.id = null;
        this.name = new Name(name);
        this.date = Objects.requireNonNull(date, errorMessage);
        this.time = Objects.requireNonNull(time, errorMessage);
        this.theme = Objects.requireNonNull(theme, errorMessage);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        final String errorMessage = "인자 중 null 값이 존재합니다.";
        this.id = Objects.requireNonNull(id);
        this.name = new Name(name);
        this.date = Objects.requireNonNull(date, errorMessage);
        this.time = Objects.requireNonNull(time, errorMessage);
        this.theme = Objects.requireNonNull(theme, errorMessage);
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

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
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
