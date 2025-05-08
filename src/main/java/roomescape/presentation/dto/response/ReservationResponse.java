package roomescape.presentation.dto.response;

import roomescape.domain.model.Member;
import roomescape.domain.model.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        StartAtResponse time,
        ReservationThemeResponse theme
) {

    public static ReservationResponse of(final Reservation reservation, final Member member) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(member),
                reservation.getDate(),
                StartAtResponse.from(reservation.getTime()),
                ReservationThemeResponse.from(reservation.getTheme())
        );
    }
}
