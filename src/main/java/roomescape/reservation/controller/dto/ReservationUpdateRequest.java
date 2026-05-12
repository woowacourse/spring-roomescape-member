package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.reservation.exception.InvalidReservationRequestException;
import roomescape.reservation.service.dto.ReservationUpdateCommand;

public record ReservationUpdateRequest(LocalDate date, Long timeId) {

    public ReservationUpdateRequest {
        if (date == null && timeId == null) {
            throw new InvalidReservationRequestException();
        }
    }

    public ReservationUpdateCommand toCommand() {
        return new ReservationUpdateCommand(
                date,
                timeId
        );
    }
}
