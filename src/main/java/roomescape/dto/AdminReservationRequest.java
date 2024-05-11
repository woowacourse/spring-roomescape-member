package roomescape.dto;

import java.time.LocalDate;

public class AdminReservationRequest {

    private final LocalDate date;
    private final Long themeId;
    private final Long timeId;
    private final Long memberId;


    public AdminReservationRequest(LocalDate date, Long themeId, Long timeId, Long memberId) {
        this.date = date;
        this.themeId = themeId;
        this.timeId = timeId;
        this.memberId = memberId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
