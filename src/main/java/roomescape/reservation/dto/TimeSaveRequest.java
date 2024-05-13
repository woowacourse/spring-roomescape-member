package roomescape.reservation.dto;

import roomescape.global.exception.RoomEscapeException;

import java.time.LocalTime;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static roomescape.global.exception.ExceptionMessage.START_AT_CANNOT_NULL;

public record TimeSaveRequest(
        Long id,
        LocalTime startAt
) {
    public TimeSaveRequest {
        try {
            Objects.requireNonNull(startAt, START_AT_CANNOT_NULL.getMessage());
        } catch (NullPointerException e) {
            throw new RoomEscapeException(BAD_REQUEST, e.getMessage());
        }
    }
}
