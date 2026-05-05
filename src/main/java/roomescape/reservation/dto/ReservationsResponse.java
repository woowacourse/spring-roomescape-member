package roomescape.reservation.dto;

import roomescape.reservation.model.Reservation;

import java.util.List;

public class ReservationsResponse {

    private final List<ReservationResponse> reservationsResponse;

    private ReservationsResponse(List<ReservationResponse> reservationsDto) {
        this.reservationsResponse = reservationsDto;
    }

    public static ReservationsResponse from(List<Reservation> reservations) {
        List<ReservationResponse> responses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return new ReservationsResponse(responses);
    }

    public List<ReservationResponse> getReservationsResponse() {
        return reservationsResponse;
    }
}
