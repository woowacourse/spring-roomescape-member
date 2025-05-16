package roomescape.reservation.service.dto.response;

import roomescape.auth.entity.Member;
import roomescape.auth.service.dto.LoginMember;
import roomescape.theme.service.dto.response.ThemeResponse;
import roomescape.theme.entity.Theme;
import roomescape.time.service.dto.response.ReservationTimeResponse;
import roomescape.reservation.entity.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String memberName,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse of(Reservation reservation, Theme theme, LoginMember member) {
        return new ReservationResponse(
                reservation.getId(),
                member.name(),
                reservation.getDate(),
                ReservationTimeResponse.of(reservation.getTime()),
                ThemeResponse.of(theme)
        );
    }

    public static ReservationResponse of(Reservation reservation, Theme theme, Member member) {
        return new ReservationResponse(
                reservation.getId(),
                member.getName(),
                reservation.getDate(),
                ReservationTimeResponse.of(reservation.getTime()),
                ThemeResponse.of(theme)
        );
    }
}
