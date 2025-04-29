package roomescape.reservation_time.application.usecase;

import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.ReservationTimeId;

import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeQueryUseCase {

    ReservationTime get(ReservationTimeId id);

    List<ReservationTime> getAll();

    boolean existsByStartAt(LocalTime time);
}
