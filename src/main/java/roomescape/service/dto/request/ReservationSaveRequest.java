package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationSaveRequest(@NotBlank(message = "예약자 이름을 입력해주세요.") String name,
                                     @NotNull(message = "예약 날짜를 입력해주세요.") LocalDate date,
                                     @NotNull(message = "예약 시간을 입력해주세요.") Long timeId,
                                     @NotNull(message = "예약 테마를 입력해주세요.") Long themeId) {

    public static Reservation toEntity(ReservationSaveRequest request, ReservationTime reservationTime, Theme theme) {
        return new Reservation(request.name(), request.date(), reservationTime, theme);
    }
}
