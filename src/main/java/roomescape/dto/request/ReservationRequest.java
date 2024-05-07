package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

public record ReservationRequest(
        @NotBlank(message = "사용자 이름 입력이 존재하지 않습니다.") String name,
        @NotNull(message = "예약 날짜 입력이 존재하지 않습니다.") LocalDate date,
        @NotNull(message = "예약 시간 입력이 존재하지 않습니다.") Long timeId,
        @NotNull(message = "테마 입력이 존재하지 않습니다.") Long themeId) {
    public Reservation toReservation(ReservationTime reservationTime, RoomTheme roomTheme) {
        return new Reservation(new Name(name), date, reservationTime, roomTheme);
    }
}
