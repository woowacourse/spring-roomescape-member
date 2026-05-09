package roomescape.reservation.exception;

import roomescape.common.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {

    public ReservationNotFoundException(Long id) {
        super("존재하지 않는 예약입니다. id=" + id);
    }
}
