package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.controller.exception.valid.DateValid;

public record ReservationRequest(
        @NotBlank(message = "이름을 입력해주세요.")
        @Size(min = 1, max = 255, message = "이름은 1자 이상 255자 이하로 입력해주세요.")
        String name,

        @NotBlank(message = "날짜를 입력해주세요.")
        @DateValid
        String date,

        @NotNull(message = "예약 시간 id을 입력해주세요.")
        @Positive
        Long timeId,

        @NotNull(message = "테마 id을 입력해주세요.")
        @Positive
        Long themeId
) {

    public Reservation toReservation(ReservationTime time, Theme theme) {
        return new Reservation(name, LocalDate.parse(date), time, theme);
    }
}
