package roomescape.dto;

import roomescape.entity.ReservationEntity;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse timeDto
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
