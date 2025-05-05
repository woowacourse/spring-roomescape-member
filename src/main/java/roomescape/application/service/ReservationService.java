package roomescape.application.service;

import java.util.List;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;

public interface ReservationService {
    List<ReservationResponse> findAllReservations();

    ReservationResponse createReservation(ReservationRequest request);

    void deleteReservation(Long id);
}
