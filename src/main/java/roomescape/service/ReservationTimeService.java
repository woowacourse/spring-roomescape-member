package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;

import java.util.List;

@Service
public interface ReservationTimeService {
    ReservationTimeResponse save(final ReservationTimeRequest reservationTimeRequest);

    List<ReservationTimeResponse> findAll();

    void delete(final long id);
}
