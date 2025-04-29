package roomescape.time.service;


import java.util.List;
import roomescape.time.controller.request.ReservationTimeCreateRequest;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;

public interface ReservationTimeService {

    ReservationTimeResponse create(ReservationTimeCreateRequest request);

    List<ReservationTimeResponse> getAll();

    ReservationTime getReservationTime(Long id);

    void deleteById(Long id);
}
