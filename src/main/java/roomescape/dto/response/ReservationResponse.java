package roomescape.dto.response;

import roomescape.entity.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
    Long id,
    LoginCheckResponse member,
    LocalDate date,
    ReservationTimeResponse time,
    ThemeResponse theme) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            LoginCheckResponse.from(reservation.getMember().getName()),
            reservation.getDate(),
            ReservationTimeResponse.of(reservation.getTime()),
            ThemeResponse.of(reservation.getTheme())
        );
    }
}
