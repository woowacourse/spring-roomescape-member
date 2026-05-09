package roomescape.controller.dto;

import java.time.LocalDate;
import roomescape.service.dto.ServiceReservationResponse;

public record ControllerReservationResponse(Long id, String name, LocalDate date,
                                            ControllerReservationTimeResponse time,
                                            ControllerThemeResponse theme) {
    public static ControllerReservationResponse from(ServiceReservationResponse response) {
        return new ControllerReservationResponse(
                response.id(),
                response.name(),
                response.date(),
                ControllerReservationTimeResponse.from(response.time()),
                ControllerThemeResponse.from(response.theme()));
    }
}
