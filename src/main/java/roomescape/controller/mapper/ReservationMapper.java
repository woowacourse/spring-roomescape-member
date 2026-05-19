package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationUpdateRequest;
import roomescape.domain.EntityId;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationUpdateCommand;

@Component
public class ReservationMapper {

    public ReservationCreateCommand mapToCreateCommand(
            ReservationCreateRequest request
    ) {
        return new ReservationCreateCommand(
                request.name(),
                request.date(),
                EntityId.fromUuid(request.timeId()),
                EntityId.fromUuid(request.themeId())
        );
    }

    public ReservationUpdateCommand mapToUpdateCommand(
            EntityId reservationId,
            String name,
            ReservationUpdateRequest request
    ) {
        return new ReservationUpdateCommand(
                reservationId,
                name,
                request.date(),
                EntityId.fromUuid(request.timeId())
        );
    }
}
