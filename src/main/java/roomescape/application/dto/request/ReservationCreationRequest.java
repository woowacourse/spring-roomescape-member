package roomescape.application.dto.request;

import java.time.LocalDate;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public record ReservationCreationRequest(LocalDate date, long timeId, long themeId, long memberId) {
    public Reservation toReservation(ReservationTime time, Theme theme, Member member) {
        return new Reservation(date, time, theme, member);
    }
}
