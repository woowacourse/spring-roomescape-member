package roomescape.reservation.application.usecase;

import roomescape.reservation.application.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationId;

public interface ReservationCommandUseCase {

    Reservation create(CreateReservationServiceRequest createReservationServiceRequest);

    void delete(ReservationId id);
}
