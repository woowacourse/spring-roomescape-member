package roomescape.reservation.dto;

import java.time.LocalDate;
import java.util.Objects;

public class SearchInfo {
    private final Long memberId;
    private final Long themeId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public SearchInfo(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo) {
        this.memberId = memberId;
        this.themeId = themeId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public boolean isMemberIdNotNull() {
        return memberId != null;
    }

    public boolean isThemeIdNotNull() {
        return themeId != null;
    }

    public boolean isDurationNotNull() {
        return dateFrom != null && dateTo != null;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchInfo that = (SearchInfo) o;
        return Objects.equals(memberId, that.memberId) && Objects.equals(themeId, that.themeId) && Objects.equals(dateFrom, that.dateFrom) && Objects.equals(dateTo, that.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, themeId, dateFrom, dateTo);
    }

    @Override
    public String toString() {
        return "SearchInfo{" +
                "memberId=" + memberId +
                ", themeId=" + themeId +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }
}
