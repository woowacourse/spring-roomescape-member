package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(
        @NotBlank(message = "name을 입력해주세요.")
        String name,

        @NotNull(message = "date를 입력해주세요.")
        LocalDate date,

        @NotNull(message = "timeId를 입력해주세요.")
        Long timeId,

        @NotNull(message = "themeId를 입력해주세요.")
        Long themeId) {
    public Reservation toReservation(ReservationTime time, Theme theme) {
        return new Reservation(name, date, time, theme);
    }
}
