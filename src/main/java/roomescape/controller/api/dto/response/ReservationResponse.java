package roomescape.controller.api.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.service.dto.query.ReservationQuery;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeInfo time
) {

    private record ReservationTimeInfo(
            Long id,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
            LocalTime startAt
    ) {
    }

    public static ReservationResponse from(ReservationQuery query) {
        return new ReservationResponse(
                query.id(),
                query.name(),
                query.date(),
                new ReservationTimeInfo(query.timeId(), query.startAt())
        );
    }
}
