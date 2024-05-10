package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import roomescape.domain.util.Validator;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {

    public static final int MAX_STRING_LENGTH = 255;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation {
        Validator.nonNull(name, date, time, theme);
        Validator.notEmpty(name);
        Validator.overSize(MAX_STRING_LENGTH, name);
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
        return time.id();
    }

    public Long getThemeId() {
        return theme.id();
    }
}
