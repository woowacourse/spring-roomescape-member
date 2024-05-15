package roomescape.time.dto;

import jakarta.validation.constraints.NotBlank;

import roomescape.time.domain.ReservationTime;

public record ReservationTimeRequestDto(
        @NotBlank(message = "예약 시간을 입력해야 합니다.") String startAt
) {
    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
