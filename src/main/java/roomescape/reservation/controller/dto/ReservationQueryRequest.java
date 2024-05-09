package roomescape.reservation.controller.dto;

import java.time.LocalDate;

public class ReservationQueryRequest {
    private final Long themeId;
    private final Long memberId;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public ReservationQueryRequest(Long themeId, Long memberId, LocalDate startDate, LocalDate endDate) {
        initialize(startDate, endDate);
        this.themeId = themeId;
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void initialize(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusMonths(2);
        }
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
