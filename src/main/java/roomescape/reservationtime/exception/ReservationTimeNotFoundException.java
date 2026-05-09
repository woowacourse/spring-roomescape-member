package roomescape.reservationtime.exception;

import roomescape.common.exception.NotFoundException;

public class ReservationTimeNotFoundException extends NotFoundException {

    public ReservationTimeNotFoundException(Long id) {
        super("존재하지 않는 예약 시간입니다. id=" + id);
    }
}
