package roomescape.dto;

import roomescape.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationResponse {

    private final long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponse time;

    private ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime())
        );
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
        return time.getStartAt();
    }
}
