package roomescape.time.application;

import roomescape.time.ui.dto.CreateReservationTimeWebRequest;
import roomescape.time.ui.dto.ReservationTimeResponse;

import java.util.List;

public interface ReservationTimeService {

    List<ReservationTimeResponse> getAll();

    ReservationTimeResponse create(CreateReservationTimeWebRequest createReservationTimeWebRequest);

    void delete(Long id);
}
