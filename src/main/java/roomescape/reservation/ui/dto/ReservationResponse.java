package roomescape.reservation.ui.dto;

import java.time.LocalDate;
import roomescape.member.ui.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.ui.dto.ThemeResponse;

public record ReservationResponse(
        Long id,
        MemberResponse.Name member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.Name.from(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()));
    }
}
