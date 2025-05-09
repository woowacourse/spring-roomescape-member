package roomescape.reservation.ui.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.ui.dto.CreateThemeResponse;

public record MemberReservationResponse(
        Long id,
        String name,
        LocalDate date,
        CreateReservationTimeResponse time,
        CreateThemeResponse theme
) {

    public static MemberReservationResponse from(final Reservation reservation) {
        return new MemberReservationResponse(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getDate(),
                CreateReservationTimeResponse.from(reservation.getTime()),
                CreateThemeResponse.from(reservation.getTheme()));
    }
}
