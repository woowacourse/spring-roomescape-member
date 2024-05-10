package roomescape.dto.reservation;

import java.util.Objects;

public class ReservationFilterRequest {

    private final Long memberId;
    private final Long themeId;
    private final String dateFrom;
    private final String dateTo;

    public ReservationFilterRequest(Long memberId, Long themeId, String dateFrom, String dateTo) {
        this.memberId = memberId;
        this.themeId = themeId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationFilterRequest other = (ReservationFilterRequest) o;
        return Objects.equals(this.memberId, other.memberId)
               && Objects.equals(this.themeId, other.themeId)
               && Objects.equals(this.dateFrom, other.dateFrom)
               && Objects.equals(this.dateTo, other.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, themeId, dateFrom, dateTo);
    }

    @Override
    public String toString() {
        return "ReservationFilterRequest{" +
               "dateFrom='" + dateFrom + '\'' +
               ", memberId=" + memberId +
               ", themeId=" + themeId +
               ", dateTo='" + dateTo + '\'' +
               '}';
    }
}
