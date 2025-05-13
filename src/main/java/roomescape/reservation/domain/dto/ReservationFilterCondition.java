package roomescape.reservation.domain.dto;

import java.time.LocalDate;
import java.util.Optional;

public class ReservationFilterCondition {

    private final Long themeId;
    private final Long memberId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public ReservationFilterCondition(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        this.themeId = themeId;
        this.memberId = memberId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Optional<Long> getThemeId() {
        return Optional.ofNullable(themeId);
    }

    public Optional<Long> getMemberId() {
        return Optional.ofNullable(memberId);
    }

    public Optional<LocalDate> getDateFrom() {
        return Optional.ofNullable(dateFrom);
    }

    public Optional<LocalDate> getDateTo() {
        return Optional.ofNullable(dateTo);
    }
}
