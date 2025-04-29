package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeRequest(
    @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public static ReservationTime toEntity(TimeRequest request) {
        return new ReservationTime(null, request.startAt());
    }
}
