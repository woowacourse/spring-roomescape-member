package roomescape.domain;

import java.time.LocalDate;

public record Reservation(long id, String name, LocalDate date, ReservationTime time, Theme theme) {

    public Reservation(long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = validateName(name);
        this.date = date;
        this.time = time;
        this.theme = theme;
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
