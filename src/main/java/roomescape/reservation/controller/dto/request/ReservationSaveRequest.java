package roomescape.reservation.controller.dto.request;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationSaveRequest(
        LocalDate date,
        long timeId,
        long themeId
) {
    public Reservation toEntity(final Member member, final ReservationTime reservationTime, final Theme theme) {
        return Reservation.of(member, date, reservationTime, theme);
    }
}
