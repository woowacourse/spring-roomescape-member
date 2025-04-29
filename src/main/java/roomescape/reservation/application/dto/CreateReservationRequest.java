package roomescape.reservation.application.dto;

import roomescape.reservation.domain.aggregate.ReservationDate;
import roomescape.reservation.domain.aggregate.ReservationName;
import roomescape.reservation.domain.aggregate.ReservationTime;

public class CreateReservationRequest {
    private final ReservationName name;
    private final ReservationDate date;
    private final ReservationTime time;

    public CreateReservationRequest(ReservationName name, ReservationDate date, ReservationTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public ReservationName getName() {
        return name;
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
