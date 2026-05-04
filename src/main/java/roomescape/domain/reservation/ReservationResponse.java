package roomescape.domain.reservation;

import roomescape.domain.reservationtime.ReservationTimeResponse;

import java.time.LocalDate;

public class ReservationResponse {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponse time;

    public ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
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

    public ReservationTimeResponse getTime() {
        return time;
    }
}
