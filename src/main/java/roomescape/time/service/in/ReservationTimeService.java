package roomescape.time.service.in;


import java.util.List;
import roomescape.time.controller.request.AvailableReservationTimeRequest;
import roomescape.time.controller.request.ReservationTimeCreateRequest;
import roomescape.time.controller.response.AvailableReservationTimeResponse;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;

public interface ReservationTimeService {

    ReservationTimeResponse open(ReservationTimeCreateRequest request);

    List<ReservationTimeResponse> getAll();

    ReservationTime getReservationTime(Long id);

    void deleteById(Long id);

    List<AvailableReservationTimeResponse> getAvailableReservationTimes(AvailableReservationTimeRequest request);
}
