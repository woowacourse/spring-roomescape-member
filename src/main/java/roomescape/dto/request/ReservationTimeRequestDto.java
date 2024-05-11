package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ReservationTimeRequestDto {
    @NotBlank(message = "예약 시간은 null이나 빈 값일 수 없습니다.")
    private String startAt;

    public ReservationTimeRequestDto() {
    }

    public ReservationTimeRequestDto(final String startAt) {
        this.startAt = startAt;
    }

    public String getStartAt() {
        return startAt;
    }
}
