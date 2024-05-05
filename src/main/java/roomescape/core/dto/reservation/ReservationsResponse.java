package roomescape.core.dto.reservation;

import java.util.List;

public class ReservationsResponse {
    private List<ReservationResponse> reservations;

    public ReservationsResponse() {
    }

    public ReservationsResponse(final List<ReservationResponse> reservations) {
        this.reservations = reservations;
    }

    public List<ReservationResponse> getReservations() {
        return reservations;
    }
}
