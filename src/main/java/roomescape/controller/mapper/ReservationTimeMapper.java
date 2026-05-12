package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.service.dto.ReservationTimeCreateCommand;

@Component
public class ReservationTimeMapper {

    public ReservationTimeCreateCommand mapToCommand(
            ReservationTimeCreateRequest request
    ) {
        return new ReservationTimeCreateCommand(request.startAt());
    }
}
