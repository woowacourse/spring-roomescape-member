package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.entity.ReservationTime;

public record ReservationTimeResponse(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public ReservationTimeResponse(final ReservationTime reservationTime) {
        this(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
