package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.reservation.exception.InvalidReservationRequestException;
import roomescape.reservation.service.dto.ReservationCommand;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public ReservationRequest {
        if (name == null || name.isBlank()) {
            throw new InvalidReservationRequestException();
        }

        if (date == null) {
            throw new InvalidReservationRequestException();
        }

        if (timeId == null) {

        }
    }


    public ReservationCommand toCommand() {
        return new ReservationCommand(
                name,
                date,
                timeId,
                themeId
        );
    }
}
