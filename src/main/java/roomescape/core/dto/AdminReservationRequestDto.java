package roomescape.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AdminReservationRequestDto {
    @NotBlank(message = "예약 날짜는 null이나 빈 값일 수 없습니다.")
    private String date;
    @NotNull(message = "예약 시간 id는 null일 수 없습니다.")
    private Long timeId;
    @NotNull(message = "예약 테마 id는 null일 수 없습니다.")
    private Long themeId;
    @NotNull(message = "예약 회원 id는 null일 수 없습니다.")
    private Long memberId;

    public AdminReservationRequestDto() {
    }

    public AdminReservationRequestDto(
            final String date,
            final Long timeId,
            final Long themeId,
            final Long memberId
    ) {
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
        this.memberId = memberId;
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

    public Long getMemberId() {
        return memberId;
    }
}
