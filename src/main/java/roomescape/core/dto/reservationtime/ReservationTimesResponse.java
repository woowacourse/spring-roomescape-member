package roomescape.core.dto.reservationtime;

import java.util.List;

public class ReservationTimesResponse {
    private List<ReservationTimeResponse> times;

    public ReservationTimesResponse() {
    }

    public ReservationTimesResponse(final List<ReservationTimeResponse> times) {
        this.times = times;
    }

    public List<ReservationTimeResponse> getTimes() {
        return times;
    }
}
