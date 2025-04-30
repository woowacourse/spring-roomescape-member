package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

//TODO: id 빼고 response 를 만들어야 할까?
public record ReservationTimeResponse(
    Long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt
) {

    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(
            time.getId(),
            time.getStartAt()
        );
    }
}
