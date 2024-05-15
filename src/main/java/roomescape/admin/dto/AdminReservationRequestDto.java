package roomescape.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import roomescape.reservation.dto.ReservationRequestDto;

public record AdminReservationRequestDto(
        @NotBlank(message = "예약날짜를 입력해야 합니다.") String date,
        @Min(value = 1, message = "올바른 예약 시간 ID를 입력해야 합니다.") long timeId,
        @Min(value = 1, message = "올바른 예약 테마 ID를 입력해야 합니다.") long themeId,
        @Min(value = 1, message = "올바른 사용자 ID를 입력해야 합니다.") long memberId
) {
    public ReservationRequestDto toReservationRequestDto() {
        return new ReservationRequestDto(date, timeId, themeId);
    }
}
