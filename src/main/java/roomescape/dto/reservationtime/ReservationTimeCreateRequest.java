package roomescape.dto.reservationtime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;

public record ReservationTimeCreateRequest(
        @NotBlank(message = "예약 시간을 입력해주세요")
        @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$", message = "HH-mm 형식이 아닙니다.")
        String startAt) {

    public static ReservationTimeCreateRequest from(String startAt) {
        return new ReservationTimeCreateRequest(startAt);
    }

    public ReservationTime toDomain() {
        return new ReservationTime(
                null,
                ReservationStartAt.from(startAt)
        );
    }
}
