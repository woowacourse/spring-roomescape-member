package roomescape.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.reservation.model.Reservation;

public class ReservationResponseDto {
    private long id;
    private final String name;
    private final LocalDate date;
    private final LocalTime time;

    public ReservationResponseDto(long id, Reservation reservation) {
        this.id = id;
        this.name = reservation.getName();
        this.date = reservation.getDate();
        this.time = reservation.getTime();
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

    public LocalTime getTime() {
        return time;
    }
}
