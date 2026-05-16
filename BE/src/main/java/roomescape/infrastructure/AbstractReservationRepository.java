package roomescape.infrastructure;

import roomescape.entity.Reservation;
import roomescape.entity.ReservationRepository;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.NotFoundException;

public abstract class AbstractReservationRepository implements ReservationRepository {

    @Override
    public Reservation getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND_BY_ID));
    }
}
