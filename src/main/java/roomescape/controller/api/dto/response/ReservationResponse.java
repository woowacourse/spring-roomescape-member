package roomescape.controller.api.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.service.dto.response.ReservationServiceResponse;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        InnerReservationTime time,
        InnerReservationTheme theme
) {

    private record InnerReservationTime(
            Long id,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
            LocalTime startAt
    ) {
    }

    public static ReservationResponse from(ReservationServiceResponse response) {
        return new ReservationResponse(
                response.id(),
                response.name(),
                response.date(),
                new InnerReservationTime(response.time().id(), response.time().startAt()),
                new InnerReservationTheme(response.theme().id(), response.theme().name(),
                        response.theme().description(), response.theme().thumbnail())
        );
    }

    private record InnerReservationTheme(
            Long id,
            String name,
            String description,
            String thumbnail
    ) {
    }
}
