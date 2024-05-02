package roomescape.core.dto;

public class ReservationRequest {
    private String date;
    private String name;
    private Long timeId;
    private Long themeId;

    public ReservationRequest() {
    }

    public ReservationRequest(final String date, final String name, final Long timeId, final Long themeId) {
        this.date = date;
        this.name = name;
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
