package roomescape.reservation.dto;

import java.time.LocalDate;

public record SearchRequest(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo) {

    public SearchInfo toSearchInfo() {
        return new SearchInfo(memberId, themeId, dateFrom, dateTo);
    }
}
