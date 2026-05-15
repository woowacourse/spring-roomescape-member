package roomescape.controller.dto;

public class ReservationUpdateRequest {
    private String date;
    private Long timeId;

    public ReservationUpdateRequest() {
    }

    public ReservationUpdateRequest(String date, Long timeId) {
        this.date = date;
        this.timeId = timeId;
    }

    public String getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }
}
