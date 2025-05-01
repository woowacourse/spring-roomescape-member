package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record StartAtResponse(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public static StartAtResponse from(ReservationTime time) {
        return new StartAtResponse(time.getStartAt());
    }
}
