package roomescape.time.service;

import roomescape.time.controller.dto.CreateReservationTimeWebRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;

import java.util.List;

public interface ReservationTimeService {

    List<ReservationTimeResponse> getAll();

    ReservationTimeResponse create(CreateReservationTimeWebRequest createReservationTimeWebRequest);

    void delete(Long id);
}
