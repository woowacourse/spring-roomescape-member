package roomescape.dto.reservationtime.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalTime;
import roomescape.domain.reservationtime.ReservationTime;

public record ReservationTimeRequestDto(

        @NotBlank
        @Pattern(
                regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
                message = "시간 형식이 틀렸습니다. HH:mm (예: 14:30) 이어야 합니다.")
        String startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(LocalTime.parse(startAt));
    }

}
