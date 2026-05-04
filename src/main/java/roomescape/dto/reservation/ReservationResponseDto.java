package roomescape.dto.reservation;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record ReservationResponseDto(
        Long id,
        String name,
        String date,
        ReservationTime time) {

    public static ReservationResponseDto from(Reservation reservation) {
        String name = reservation.getName().value();
        String date = reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);

        return new ReservationResponseDto(reservation.getId(), name, date, reservation.getTime());
    }
}
