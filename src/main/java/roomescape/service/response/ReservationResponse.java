package roomescape.service.response;

import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        String date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        MemberResponse memberResponse = MemberResponse.from(reservation.getMember());
        String date = reservation.getDate().date().toString();
        ReservationTimeResponse timeResponse = ReservationTimeResponse.from(reservation.getTime());
        ThemeResponse themeResponse = ThemeResponse.from(reservation.getTheme());

        return new ReservationResponse(
                reservation.getId(),
                memberResponse,
                date,
                timeResponse,
                themeResponse
        );
    }
}
