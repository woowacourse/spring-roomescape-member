package roomescape.admin.dto;

import java.time.LocalDate;
import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public record SaveAdminReservationRequest(
    LocalDate date,
    Long themeId,
    Long timeId
) {

  public Reservation toReservation(
      final Member member,
      final ReservationTime reservationTime,
      final Theme theme
  ) {
    return Reservation.createInstanceWithoutId(
        member,
        date,
        reservationTime,
        theme
    );
  }
}
