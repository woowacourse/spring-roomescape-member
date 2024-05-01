package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import roomescape.time.domain.ReservationTime;

public class Reservation {

    private final long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(long id, String name, LocalDate date, ReservationTime time) {
        this.id = id;
        this.name = validateName(name);
        LocalDateTime dateTime = validateDateTime(date, time.startAt());
        this.date = dateTime.toLocalDate();
        this.time = time;
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

    private LocalDateTime validateDateTime(LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create a reservation for a past date and time.");
        }
        return dateTime;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
