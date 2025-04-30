package roomescape.time.application.usecase;

import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeQueryUseCase {

    ReservationTime get(ReservationTimeId id);

    List<ReservationTime> getAll();

    boolean existsByStartAt(LocalTime time);
}
