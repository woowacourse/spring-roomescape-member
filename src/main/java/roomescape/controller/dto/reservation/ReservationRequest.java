package roomescape.controller.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import roomescape.service.dto.reservation.CreateReservationCommand;

public record ReservationRequest(
        @NotBlank(message = "예약자 이름은 필수입니다.")
        @Size(min = 2, max = 20, message = "예약자 이름은 2자 이상 20자 이하여야 합니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z ]+$", message = "예약자 이름은 완성형 한글, 영문, 공백만 허용합니다.")
        String name,

        @NotNull(message = "예약 날짜는 필수입니다.")
        LocalDate date,

        @NotNull(message = "예약 시간은 필수입니다.")
        Long timeId,

        @NotNull(message = "테마는 필수입니다.")
        Long themeId
) {

    public CreateReservationCommand toCommand() {
        return new CreateReservationCommand(
                name.trim(),
                date,
                timeId,
                themeId
        );
    }
}
