package roomescape.controller.dto.reservationtime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalTime;
import roomescape.service.dto.CreateReservationTimeCommand;

public record ReservationTimeRequest(
        @NotBlank(message = "예약 시간은 필수입니다.")
        @Pattern(regexp = "\\d{2}:\\d{2}", message = "예약 시간 형식은 HH:mm 이어야 합니다.")
        String startAt
) {

    public CreateReservationTimeCommand toCommand() {
        return new CreateReservationTimeCommand(LocalTime.parse(startAt));
    }
}
