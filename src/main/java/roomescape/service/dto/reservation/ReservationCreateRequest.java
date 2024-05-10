package roomescape.service.dto.reservation;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationCreateRequest(
        Long memberId,
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public Reservation toReservation(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(member, date, reservationTime, theme);
    }

    public ReservationCreateRequest withMemberId(long memberId) {
        return new ReservationCreateRequest(memberId, date, timeId, themeId);
    }
}
