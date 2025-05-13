package roomescape.controller.reservationTime.dto;

import java.time.LocalTime;
import roomescape.model.AvailableReservationTime;

public record AvailableReservationTimeResponseDto(
        Long timeId,
        LocalTime startAt,
        boolean alreadyBooked
) {
    public static AvailableReservationTimeResponseDto from(AvailableReservationTime availableReservationTime) {
        return new AvailableReservationTimeResponseDto(
                availableReservationTime.reservationTime().getId(),
                availableReservationTime.reservationTime().getStartAt(),
                availableReservationTime.alreadyBooked()
        );
    }
}
