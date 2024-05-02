package roomescape.core.dto;

public class ReservationTimeRequest {
    private String startAt;

    public ReservationTimeRequest() {
    }

    public ReservationTimeRequest(final String startAt) {
        this.startAt = startAt;
    }

    public String getStartAt() {
        return startAt;
    }
}
