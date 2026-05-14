package roomescape.reservation.service.dto;

import java.time.LocalDate;
import roomescape.reservation.controller.dto.UpdateReservationRequest;

public record RescheduleReservationInfo(
        Long id,
        LocalDate date,
        Long timeId
) {
    public static RescheduleReservationInfo of(
            Long reservationId,
            UpdateReservationRequest updateReservationRequest) {
        return new RescheduleReservationInfo(
                reservationId,
                updateReservationRequest.date(),
                updateReservationRequest.timeId()
        );
    }
}
