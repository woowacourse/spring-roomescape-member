package roomescape.reservation_time.application.usecase;

import roomescape.reservation_time.application.dto.CreateReservationTimeServiceRequest;
import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.ReservationTimeId;

public interface ReservationTimeCommandUseCase {

    ReservationTime create(CreateReservationTimeServiceRequest createReservationTimeServiceRequest);

    void delete(ReservationTimeId id);
}
