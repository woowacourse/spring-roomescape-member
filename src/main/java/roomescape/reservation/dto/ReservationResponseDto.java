package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.time.dto.ReservationTimeResponseDto;

public record ReservationResponseDto(long id, String name, String date, ReservationTimeResponseDto time) {

    public ReservationResponseDto(final Reservation reservation) {
        this(reservation.id(), reservation.name(), reservation.date().toString(), new ReservationTimeResponseDto(reservation.time()));
    }
}
