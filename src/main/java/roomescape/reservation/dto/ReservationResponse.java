package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.auth.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.theme.dto.ThemeResponse;

public record ReservationResponse(
    Long id,
    MemberResponse member,
    LocalDate date,
    ReservationTimeResponse time,
    ThemeResponse theme
) {

  public static ReservationResponse from(final Reservation reservation) {
    return new ReservationResponse(
        reservation.getId(),
        MemberResponse.from(reservation.getMember()),
        reservation.getDate().getValue(),
        ReservationTimeResponse.from(reservation.getTime()),
        ThemeResponse.from(reservation.getTheme())
    );
  }
}
