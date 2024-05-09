package roomescape.web.api.dto;

import java.util.List;

public class ReservationTimeListResponse {
    private List<?> reservationTimes;

    private ReservationTimeListResponse() {
    }

    public ReservationTimeListResponse(List<?> reservationTimes) {
        this.reservationTimes = reservationTimes;
    }

    public List<?> getReservationTimes() {
        return reservationTimes;
    }
}
