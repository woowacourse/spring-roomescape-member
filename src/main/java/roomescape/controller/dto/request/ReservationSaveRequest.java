package roomescape.controller.dto.request;

import java.time.LocalDate;
import roomescape.domain.member.MemberName;
import roomescape.domain.roomescape.Reservation;
import roomescape.domain.roomescape.ReservationTime;
import roomescape.domain.roomescape.Theme;

public record ReservationSaveRequest(
        LocalDate date,
        long timeId,
        long themeId
) {
    public Reservation toEntity(final MemberName name, final ReservationTime reservationTime, final Theme theme) {
        return Reservation.of(name, date, reservationTime, theme);
    }
}
