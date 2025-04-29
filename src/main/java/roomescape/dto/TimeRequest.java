package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeRequest(
    @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public TimeRequest {
        validateTime(startAt);
    }

    public static ReservationTime toEntity(TimeRequest request) {
        return new ReservationTime(null, request.startAt());
    }

    private void validateTime(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시간을 선택해야 합니다.");
        }
    }
}
