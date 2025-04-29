package roomescape.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;
import roomescape.business.ReservationTime;

public record ReservationTimeResponseDto(@JsonProperty("id") long id,
                                         @JsonProperty("startAt") @JsonFormat(pattern = "hh:mm") LocalTime startAt) {

    public static ReservationTimeResponseDto from(ReservationTime time) {
        return new ReservationTimeResponseDto(time.getId(), time.getStartAt());
    }
}
