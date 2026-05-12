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
                EntityId.fromString(request.timeId().toString()),
                EntityId.fromString(request.themeId().toString())
        );
    }
}
