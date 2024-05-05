package roomescape.core.dto.reservation;

public class ReservationRequest {
    private String name;
    private String date;
    private Long timeId;
    private Long themeId;

    public ReservationRequest() {
    }

    public ReservationRequest(final String name, final String date, final Long timeId, final Long themeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}
