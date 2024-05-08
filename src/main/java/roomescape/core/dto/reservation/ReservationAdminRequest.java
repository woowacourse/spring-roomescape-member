package roomescape.core.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationAdminRequest {
    @NotNull(message = "예약자 ID는 비어있을 수 없습니다.")
    private Long memberId;

    @NotBlank(message = "날짜는 비어있을 수 없습니다.")
    private String date;

    @NotNull(message = "시간 ID는 비어있을 수 없습니다.")
    private Long timeId;

    @NotNull(message = "테마 ID는 비어있을 수 없습니다.")
    private Long themeId;

    public ReservationAdminRequest() {
    }

    public ReservationAdminRequest(final Long memberId, final String date, final Long timeId, final Long themeId) {
        this.memberId = memberId;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}
