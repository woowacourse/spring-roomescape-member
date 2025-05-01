package roomescape.service.reservation;

import java.util.List;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;

public interface ReservationService {
    ReservationResponse create(ReservationRequest request);

    List<ReservationResponse> getAll();

    void deleteById(Long id);
}
