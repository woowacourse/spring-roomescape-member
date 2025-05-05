package roomescape.time.application.usecase;

import roomescape.time.application.dto.CreateReservationTimeServiceRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

public interface ReservationTimeCommandUseCase {

    ReservationTime create(CreateReservationTimeServiceRequest createReservationTimeServiceRequest);

    void delete(ReservationTimeId id);
}
