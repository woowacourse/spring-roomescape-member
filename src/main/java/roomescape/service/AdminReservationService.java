package roomescape.service;

import java.util.List;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.domain.EntityId;

public interface AdminReservationService {

    List<ReservationDetailResponse> findAllIncludeDetail();

    void delete(EntityId reservationId);
}
