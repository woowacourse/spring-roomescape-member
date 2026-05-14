package roomescape.controller.dto;

public class ReservationTimeCreateRequest {
    private final String startAt;

    public ReservationTimeCreateRequest(String startAt) {
        this.startAt = startAt;
    }

    public String getStartAt() {
        return startAt;
    }
}
