package roomescape.reservation.dto;

import roomescape.auth.principal.AuthenticatedMember;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.model.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(final Reservation reservation, final AuthenticatedMember authenticatedMember) {
        return new ReservationResponse(
                reservation.getId(),
                new MemberResponse(
                        authenticatedMember.id(),
                        authenticatedMember.name(),
                        authenticatedMember.email()
                ),
                reservation.getDate().value(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
