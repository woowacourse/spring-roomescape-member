package roomescape.domain;

import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final TimeSlot time;
    private final Theme theme;

    public Reservation(final Long id, final String name, final LocalDate date, final TimeSlot time, final Theme theme) {
        validateNameEmpty(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final String name, final LocalDate date, final TimeSlot time, final Theme theme) {
        this(null, name, date, time, theme);
    }

    private void validateNameEmpty(final String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, "name", "");
        }
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public TimeSlot getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getId() {
        return id;
    }

    public Reservation with(final Long id) {
        return new Reservation(id, name, date, time, theme);
    }
}
