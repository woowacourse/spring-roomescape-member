package roomescape.dto.reservation;

import java.time.LocalDate;

public class FilteredReservationRequest {
    private long themeId;
    private long memberId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public FilteredReservationRequest(long themeId, long memberId, LocalDate dateFrom, LocalDate dateTo) {
        this.themeId = themeId;
        this.memberId = memberId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void setThemeId(long themeId) {
        this.themeId = themeId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public long getThemeId() {
        return themeId;
    }

    public long getMemberId() {
        return memberId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }
}
