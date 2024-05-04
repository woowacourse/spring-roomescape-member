package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationAddRequest(
        @NotBlank(message = "예약자 이름은 필수입니다.") String name,
        @NotNull(message = "예약 날짜는 필수 입니다.") LocalDate date,
        @NotNull(message = "예약 시간 선택은 필수 입니다.") @Positive Long timeId,
        @NotNull(message = "테마 선택은 필수 입니다.") @Positive Long themeId) {

    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, name, date, reservationTime, theme);
    }
}
