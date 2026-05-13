package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.domain.EntityId;
import roomescape.service.dto.ReservationCreateCommand;

@Component
public class ReservationMapper {

    public ReservationCreateCommand mapToCommand(
            ReservationCreateRequest request
    ) {
        return new ReservationCreateCommand(
                request.name(),
                request.date(),
                EntityId.fromUuid(request.timeId()),
                EntityId.fromUuid(request.themeId())
        );
    }
}
