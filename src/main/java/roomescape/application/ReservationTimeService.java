package roomescape.application;


import java.util.List;
import roomescape.presentation.dto.request.AvailableReservationTimeRequest;
import roomescape.presentation.dto.request.ReservationTimeCreateRequest;
import roomescape.presentation.dto.response.AvailableReservationTimeResponse;
import roomescape.presentation.dto.response.ReservationTimeResponse;
import roomescape.domain.ReservationTime;

public interface ReservationTimeService {

    ReservationTimeResponse create(ReservationTimeCreateRequest request);

    List<ReservationTimeResponse> getAll();

    ReservationTime getReservationTime(Long id);

    void deleteById(Long id);

    List<AvailableReservationTimeResponse> getAvailableReservationTimes(AvailableReservationTimeRequest request);
}
