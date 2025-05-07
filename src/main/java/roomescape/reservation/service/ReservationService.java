package roomescape.reservation.service;

import java.util.List;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

public interface ReservationService {
    ReservationResponse create(ReservationRequest request);

    List<ReservationResponse> getAll();

    void deleteById(Long id);
}
