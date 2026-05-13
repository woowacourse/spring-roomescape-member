package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ServiceReservationRequest(
        @NotBlank(message = "[ERROR] 이름은 비어 있을 수 없습니다.")
        String name,

        @NotNull(message = "[ERROR] 날짜는 비어 있을 수 없습니다.")
        LocalDate date,

        @NotNull(message = "[ERROR] 예약 시간의 id는 비어 있을 수 없습니다.")
        Long timeId,

        @NotNull(message = "[ERROR] 테마의 id는 비어 있을 수 없습니다.")
        Long themeId
) {
    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(name, date, reservationTime, theme);
    }
}
