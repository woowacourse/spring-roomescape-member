package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public class ReservationCreateResponse {

    private final long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponse time;

    public ReservationCreateResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.name = reservation.getName();
        this.date = reservation.getDate();
        this.time = new ReservationTimeResponse(reservation.getTime());
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

    public ReservationTimeResponse getTime() {
        return time;
    }
}
