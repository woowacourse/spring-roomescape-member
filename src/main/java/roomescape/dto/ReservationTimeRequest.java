package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.entity.ReservationTime;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public ReservationTime toEntity() {
        return new ReservationTime(null, this.startAt());
    }

    public ReservationTime toEntity(long id) {
        return new ReservationTime(id, this.startAt());
    }
}
