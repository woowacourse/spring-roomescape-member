package roomescape.web.api.dto;

import java.util.List;

public class ReservationListResponse {
    private List<?> reservations;

    private ReservationListResponse() {
    }

    public ReservationListResponse(List<?> reservations) {
        this.reservations = reservations;
    }

    public List<?> getReservations() {
        return reservations;
    }
}
