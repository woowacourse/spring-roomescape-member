package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.member.dto.ReservationMemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.theme.dto.ThemeResponse;

public record ReservationResponse(Long id, ReservationMemberResponse member, LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {
    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                new ReservationMemberResponse(reservation.getName()),
                reservation.getDate(),
                new ReservationTimeResponse(
                        reservation.getTimeId(),
                        reservation.getReservationTime()
                ),
                new ThemeResponse(reservation.getThemeId(),
                        reservation.getThemeName(),
                        reservation.getThemeDescription(),
                        reservation.getThemeThumbnail())
        );
    }
}
