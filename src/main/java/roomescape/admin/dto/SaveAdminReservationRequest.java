package roomescape.admin.dto;

import java.time.LocalDate;
import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public record SaveAdminReservationRequest(
    LocalDate date,
    Long themeId,
    Long timeId,
    Long memberId
) {

  public Reservation toReservation(Member member, ReservationTime reservationTime, Theme theme) {
    return Reservation.createInstanceWithoutId(
        member,
        date,
        reservationTime,
        theme
    );
  }
}
