package roomescape.admin.domain.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.user.domain.User;

public record AdminReservationRequestDto(LocalDate date, Long themeId, Long timeId, Long memberId) {

    public Reservation toEntity(ReservationTime reservationTime, Theme theme,
                                User member) { // TODO 2025. 5. 11. 00:43: 안 쓰면 제거
        return Reservation.of(
                date,
                reservationTime,
                theme,
                member
        );
    }
}
