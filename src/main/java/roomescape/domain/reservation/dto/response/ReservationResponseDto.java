package roomescape.domain.reservation.dto.response;

import roomescape.domain.reservation.model.Reservation;
import roomescape.domain.reservationtime.dto.response.ReservationTimeResponseDto;
import roomescape.domain.theme.dto.response.ThemeResponseDto;

public record ReservationResponseDto(long id, String name, String date,
                                     ReservationTimeResponseDto time, ThemeResponseDto theme) {

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
            reservation.getId(),
            reservation.getMemberName(),
            reservation.getDate().toString(),
            ReservationTimeResponseDto.from(reservation.getReservationTime()),
            ThemeResponseDto.from(reservation.getTheme())
        );
    }
}
