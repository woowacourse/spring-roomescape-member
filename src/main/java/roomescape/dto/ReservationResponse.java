package roomescape.dto;

import roomescape.model.Reservation;

public record ReservationResponse(
        Long id,
        UserResponse user,
        String date,
        ThemeResponse theme,
        ReservationTimeResponse time
) {
    public static ReservationResponse fromEntity(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                UserResponse.fromEntity(reservation.getUser()),
                reservation.getDate().toString(),
                ThemeResponse.from(reservation.getTheme()),
                ReservationTimeResponse.fromEntity(reservation.getReservationTime())
        );
    }
}
