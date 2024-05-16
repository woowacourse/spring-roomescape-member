package roomescape.service.dto.reservation;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

import java.time.LocalDate;

public record ReservationSaveRequest(
        @NotNull(message = "예약 날짜는 널일 수 없습니다.") LocalDate date,
        @NotNull(message = "시간 Id는 널일 수 없습니다.") Long timeId,
        @NotNull(message = "테마 id는 널일 수 없습니다.") Long themeId
) {

    public Reservation toEntity(
            Member member,
            ReservationTime reservationTime,
            Theme theme) {
        return new Reservation(member, date, reservationTime, theme);
    }
}
