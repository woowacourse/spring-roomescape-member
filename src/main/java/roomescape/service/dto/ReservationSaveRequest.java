package roomescape.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationSaveRequest(
        @NotBlank(message = "예약자 이름은 빈칸일 수 없습니다.") String name,
        @NotNull(message = "예약 날짜는 널일 수 없습니다.") LocalDate date,
        @NotNull(message = "시간 Id는 널일 수 없습니다.") Long timeId,
        @NotNull(message = "테마 id는 널일 수 없습니다.") Long themeId) {

    public static Reservation toEntity(ReservationSaveRequest request, ReservationTime reservationTime, Theme theme) {
        return new Reservation(request.name(), request.date(), reservationTime, theme);
    }


}
