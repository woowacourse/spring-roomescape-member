package roomescape.controller.api.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.service.dto.response.ReservationServiceResponse;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startAt,
        String themeName
) {

    public static ReservationResponse from(ReservationServiceResponse response) {
        return new ReservationResponse(
                response.id(),
                response.name(),
                response.date(),
                response.startAt(),
                response.themeName()
        );
    }
}
