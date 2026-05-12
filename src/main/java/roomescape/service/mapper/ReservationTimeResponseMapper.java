package roomescape.service.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;

@Component
public class ReservationTimeResponseMapper {

    public ReservationTimeResponse map(
            ReservationTime reservationTime
    ) {
        return new ReservationTimeResponse(
                reservationTime.id().getValueAsString(),
                reservationTime.startAt()
        );
    }
}
