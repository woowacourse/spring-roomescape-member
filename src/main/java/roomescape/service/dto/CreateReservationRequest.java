package roomescape.service.dto;

import java.time.LocalDate;

public record CreateReservationRequest(String name, LocalDate date, long timeId, long themeId) {

    public static CreateReservationRequest from(
            final roomescape.controller.dto.request.CreateReservationRequest request) {
        return new CreateReservationRequest(request.name(), request.date(), request.timeId(), request.themeId());
    }
}
