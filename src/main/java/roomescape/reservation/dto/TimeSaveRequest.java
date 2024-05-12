package roomescape.reservation.dto;

import roomescape.global.exception.RoomEscapeException;

import java.time.LocalTime;
import java.util.Objects;

public record TimeSaveRequest(
        Long id,
        LocalTime startAt
) {
    public TimeSaveRequest {
        try {
            Objects.requireNonNull(startAt, "[ERROR] 시작 시간은 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
        }
    }
}
