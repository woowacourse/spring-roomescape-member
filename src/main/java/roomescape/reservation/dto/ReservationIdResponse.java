package roomescape.reservation.dto;

import roomescape.reservation.model.Reservation;

public class ReservationIdResponse {

    private final Long id;

    private ReservationIdResponse(Long id) {
        this.id = id;
    }

    public static ReservationIdResponse from(Reservation reservation) {
        return new ReservationIdResponse(reservation.getId());
    }

    public Long getId() {
        return id;
    }
}
