package roomescape.reservation.exception;

import roomescape.error.ErrorCode;
import roomescape.error.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
    private final Long id;

    public ReservationNotFoundException(Long id) {
        super(ErrorCode.RESERVATION_NOT_FOUND, "예약이 존재하지 않습니다. id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
