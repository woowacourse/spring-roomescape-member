package roomescape.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record AvailableReservationTimeResponse(long id, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") LocalTime startAt,
                                               boolean isBooked) {
}
