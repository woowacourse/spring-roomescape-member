package roomescape.service.response;

import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        String date,
        MemberResponse member,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        String date = reservation.getDate().toString();
        ReservationTimeResponse timeResponse = ReservationTimeResponse.from(reservation.getTime());
        ThemeResponse themeResponse = ThemeResponse.from(reservation.getTheme());
        MemberResponse memberResponse = MemberResponse.from(reservation.getMember());

        return new ReservationResponse(
                reservation.getId(),
                date,
                memberResponse,
                timeResponse,
                themeResponse
        );
    }
}
