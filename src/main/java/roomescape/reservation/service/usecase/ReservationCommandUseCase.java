package roomescape.reservation.service.usecase;

import roomescape.reservation.service.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationId;

public interface ReservationCommandUseCase {

    Reservation create(CreateReservationServiceRequest createReservationServiceRequest);

    void delete(ReservationId id);
}
