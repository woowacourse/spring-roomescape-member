package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;

@Component
public class ReservationTimeMapper {

    public ReservationTimeResponse mapToResponse(
            ReservationTime reservationTime
    ) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
