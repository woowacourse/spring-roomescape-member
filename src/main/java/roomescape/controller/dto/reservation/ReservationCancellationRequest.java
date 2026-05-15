package roomescape.controller.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import roomescape.service.dto.reservation.CancelReservationCommand;

public record ReservationCancellationRequest(
        @NotBlank(message = "예약자 이름은 필수입니다.")
        String name
) {

    public CancelReservationCommand toCommand(Long reservationId) {
        return new CancelReservationCommand(reservationId, name.trim());
    }
}
