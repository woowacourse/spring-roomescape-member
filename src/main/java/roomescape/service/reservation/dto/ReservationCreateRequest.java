package roomescape.service.reservation.dto;

import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

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
