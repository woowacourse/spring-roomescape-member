package roomescape.application.dto;

import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(
        LocalDate date,
        long timeId,
        long themeId
) {

    public Reservation toReservationWith(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                date,
                reservationTime,
                theme,
                member
        );
    }
}
