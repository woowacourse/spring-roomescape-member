package roomescape.dto;

import roomescape.domain.Reservation;

public record ReservationResponseDto(long id, String name, String date,
                                     ReservationTimeResponseDto time, ThemeResponseDto theme) {

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getDate().toString(),
                ReservationTimeResponseDto.from(reservation.getReservationTime()),
                ThemeResponseDto.from(reservation.getTheme())
        );
    }
}
