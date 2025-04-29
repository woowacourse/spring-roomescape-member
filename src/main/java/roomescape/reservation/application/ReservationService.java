package roomescape.reservation.application;

import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.ui.dto.CreateReservationWebRequest;
import roomescape.reservation.ui.dto.ReservationResponse;

import java.util.List;

public interface ReservationService {

    List<ReservationResponse> getAll();

    ReservationResponse create(CreateReservationWebRequest createReservationWebRequest);

    void delete(ReservationId id);
}
