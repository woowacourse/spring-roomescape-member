package roomescape.dto.reservation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;

public record AddReservationRequest(

        @NotBlank(message = "이름이 반드시 포함되어야 합니다.")
        @Size(min = 1, max = 20, message = "이름은 1자 이상 20자 이하이어야 합니다.")
        String name,

        @NotNull(message = "날짜가 반드시 포함되어야 합니다.")
        LocalDate date,

        @NotNull(message = "시간 ID가 반드시 포함되어야 합니다.")
        @Min(value = 1, message = "시간 ID는 1 이상이어야 합니다.")
        Long timeId,

        @NotNull(message = "테마 ID가 반드시 포함되어야 합니다.")
        @Min(value = 1, message = "테마 ID는 1 이상이어야 합니다.")
        Long themeId
) {

    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(name, date, reservationTime, theme);
    }
}
