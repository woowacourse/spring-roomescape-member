package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.service.dto.ReservationTimeCreateCommand;

@Component
public class ReservationTimeMapper {

    public ReservationTimeCreateCommand mapToCommand(
            ReservationTimeCreateRequest request
    ) {
        return new ReservationTimeCreateCommand(request.startAt());
    }

    public ReservationTimeResponse mapToResponse(
            ReservationTime reservationTime
    ) {
        return new ReservationTimeResponse(
                reservationTime.id(),
                reservationTime.startAt()
        );
    }
}
