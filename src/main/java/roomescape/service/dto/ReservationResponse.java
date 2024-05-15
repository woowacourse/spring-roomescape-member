package roomescape.service.dto;

import roomescape.domain.Reservation;

public record ReservationResponse(
        long id,
        String name,
        String date,
        ThemeResponse theme,
        ReservationTimeResponse time,
        MemberResponse member
) {
    public ReservationResponse(final Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(),
                new ThemeResponse(reservation.getTheme()),
                new ReservationTimeResponse(reservation.getReservationTime()),
                new MemberResponse(reservation.getLoginMember()));
    }
}
