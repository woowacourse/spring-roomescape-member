package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import roomescape.domain.reservation.ReservationCommand;

public record AddReservationRequest(
        @NotBlank(message = "이름이 반드시 포함되어야 합니다.")
        String name,
        @NotBlank(message = "날짜가 반드시 포함되어야 합니다.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 YYYY-MM-DD 형식이여야 합니다.")
        String date,
        @NotNull(message = "시간 ID가 반드시 포함되어야 합니다.")
        Long timeId,
        @NotNull
        Long themeId
) {
    public ReservationCommand to() {
        return new ReservationCommand(name, date, timeId, themeId);
    }
}
