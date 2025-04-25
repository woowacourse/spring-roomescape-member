package roomescape.dto;

import roomescape.domain.Reservation;

public record ReservationResponseDto(long id, String name, String date,
                                     ReservationTimeResponseDto time) {

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
            reservation.getId(),
            reservation.getPersonName(),
            reservation.getDate().toString(),
            ReservationTimeResponseDto.from(reservation.getReservationTime())
        );
    }
}
