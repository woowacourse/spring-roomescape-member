package roomescape.controller.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import roomescape.service.dto.reservation.CreateReservationCommand;

public record ReservationRequest(
        @NotBlank(message = "예약자 이름은 필수입니다.")
        @Size(max = 50, message = "예약자 이름은 50자를 넘을 수 없습니다.")
        String name,

        @NotBlank(message = "예약 날짜는 필수입니다.")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "예약 날짜 형식은 yyyy-MM-dd 이어야 합니다.")
        String date,

        @NotNull(message = "예약 시간은 필수입니다.")
        Long timeId,

        @NotNull(message = "테마는 필수입니다.")
        Long themeId
) {

    public CreateReservationCommand toCommand() {
        return new CreateReservationCommand(
                name.trim(),
                LocalDate.parse(date),
                timeId,
                themeId
        );
    }
}
