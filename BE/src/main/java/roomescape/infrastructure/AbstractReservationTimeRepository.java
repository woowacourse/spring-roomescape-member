package roomescape.infrastructure;

import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationTimeRepository;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.NotFoundException;

public abstract class AbstractReservationTimeRepository implements ReservationTimeRepository {

    @Override
    public ReservationTime getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }
}
