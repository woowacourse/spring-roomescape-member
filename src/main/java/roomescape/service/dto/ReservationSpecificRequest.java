package roomescape.service.dto;

public class ReservationSpecificRequest {

    private final Long themeId;
    private final Long memberId;
    private final String dateFrom;
    private final String dateTo;

    public ReservationSpecificRequest(Long themeId, Long memberId, String dateFrom, String dateTo) {
        this.themeId = themeId;
        this.memberId = memberId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }
}
