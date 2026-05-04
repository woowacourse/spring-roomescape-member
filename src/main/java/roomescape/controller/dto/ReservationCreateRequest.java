package roomescape.controller.dto;

public class ReservationCreateRequest {
    private final String name;
    private final String date;
    private final Long timeId;

    public ReservationCreateRequest(String name, String date, Long timeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }
}
