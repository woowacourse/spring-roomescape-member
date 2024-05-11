package roomescape.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationRequestDto {
    @NotBlank(message = "예약 날짜는 null이나 빈 값일 수 없습니다.")
    private String date;
    @NotNull(message = "예약 시간 id는 null일 수 없습니다.")
    private Long timeId;
    @NotNull(message = "예약 테마 id는 null일 수 없습니다.")
    private Long themeId;

    public ReservationRequestDto() {
    }

    public ReservationRequestDto(
            final String date,
            final Long timeId,
            final Long themeId
    ) {
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
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
