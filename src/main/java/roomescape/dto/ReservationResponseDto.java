package roomescape.dto;

import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        TimeResponseDto timeDto
) {
    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                TimeResponseDto.from(reservation.getTime())
        );
    }
}
