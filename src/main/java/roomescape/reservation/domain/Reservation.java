package roomescape.reservation.domain;

import java.time.LocalDate;

import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public record Reservation(long id, String name, LocalDate date, ReservationTime time, Theme theme) {
    private static final long UNDEFINED = 0;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(UNDEFINED, name, date, time, theme);
        validateName(name);
    }

    private String validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("Name cannot exceed 10 characters");
        }
        return name;
    }

}
