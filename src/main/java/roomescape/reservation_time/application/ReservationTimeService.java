package roomescape.reservation_time.application;

import roomescape.reservation_time.domain.ReservationTimeId;
import roomescape.reservation_time.ui.dto.CreateReservationTimeWebRequest;
import roomescape.reservation_time.ui.dto.ReservationTimeResponse;

import java.util.List;

public interface ReservationTimeService {

    List<ReservationTimeResponse> getAll();

    ReservationTimeResponse create(CreateReservationTimeWebRequest createReservationTimeWebRequest);

    void delete(ReservationTimeId id);
}
