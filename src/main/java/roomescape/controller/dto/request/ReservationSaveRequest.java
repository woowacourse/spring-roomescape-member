package roomescape.controller.dto.request;

import java.time.LocalDate;
import roomescape.domain.member.Member;
import roomescape.domain.roomescape.Reservation;
import roomescape.domain.roomescape.ReservationTime;
import roomescape.domain.roomescape.Theme;

public record ReservationSaveRequest(
        LocalDate date,
        long timeId,
        long themeId
) {
    public Reservation toEntity(final Member member, final ReservationTime reservationTime, final Theme theme) {
        return Reservation.of(member, date, reservationTime, theme);
    }
}
