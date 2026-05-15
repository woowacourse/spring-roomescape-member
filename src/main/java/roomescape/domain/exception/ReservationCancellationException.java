package roomescape.domain.exception;

import roomescape.common.exception.ConflictException;

public class ReservationCancellationException extends ConflictException {

    public ReservationCancellationException() {
        super("예약일 이틀 전까지만 취소할 수 있습니다.");
    }
}
