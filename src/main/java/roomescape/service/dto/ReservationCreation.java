package roomescape.service.dto;

import java.time.LocalDate;
import roomescape.controller.dto.request.CreateReservationRequest;

public record ReservationCreation(String name, LocalDate date, long timeId, long themeId) {

    public static ReservationCreation from(final CreateReservationRequest request) {
        return new ReservationCreation(request.name(), request.date(), request.timeId(), request.themeId());
    }
}
