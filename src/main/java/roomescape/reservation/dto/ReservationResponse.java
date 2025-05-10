package roomescape.reservation.dto;

import roomescape.member.entity.Member;
import roomescape.reservation.entity.Reservation;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.ReservationTimeResponse;

import java.time.LocalDate;

public record ReservationResponse(
        long id,
        Member member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getMember(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
