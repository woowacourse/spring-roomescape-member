package roomescape.service;

import java.util.List;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.domain.EntityId;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationUpdateCommand;

public interface ReservationService {

    ReservationSummaryResponse create(ReservationCreateCommand command);

    List<ReservationDetailResponse> findAllIncludeDetail(String name);

    ReservationSummaryResponse update(ReservationUpdateCommand command);

    ReservationSummaryResponse cancel(EntityId reservationId, String name);
}
