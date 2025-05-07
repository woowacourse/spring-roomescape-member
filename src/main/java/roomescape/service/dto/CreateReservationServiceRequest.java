package roomescape.service.dto;

import java.time.LocalDate;
import roomescape.controller.dto.request.CreateReservationRequest;

public record CreateReservationServiceRequest(String name, LocalDate date, long timeId, long themeId) {

    public static CreateReservationServiceRequest from(final CreateReservationRequest request) {
        return new CreateReservationServiceRequest(request.name(), request.date(), request.timeId(), request.themeId());
    }
}
