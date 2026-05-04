package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.service.command.ReservationTimeCreateCommand;

@Component
public class ReservationTimeMapper {

    public ReservationTimeCreateCommand mapCreateToCommand(
            ReservationTimeCreateRequest requestDto
    ) {
        return new ReservationTimeCreateCommand(
                requestDto.startAt()
        );
    }

    public ReservationTimeResponse mapToResponse(
            ReservationTime reservationTime
    ) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
