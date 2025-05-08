package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.time.entity.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public ReservationTime toEntity() {
        return new ReservationTime(null, this.startAt());
    }
}
