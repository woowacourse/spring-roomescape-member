package roomescape.reservation.dto;

import roomescape.time.dto.ReservationTimeResponse;
import roomescape.reservation.entity.ReservationEntity;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time
) {
    public static ReservationResponse from(ReservationEntity reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime())
        );
    }
}
