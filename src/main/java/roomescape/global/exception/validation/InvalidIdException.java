package roomescape.global.exception.validation;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class InvalidIdException extends ReservationException {

    public InvalidIdException() {
        super(ErrorCode.INVALID_ID, ErrorCode.INVALID_ID.getMessage());
    }

    public InvalidIdException(Long id) {
        super(ErrorCode.INVALID_ID, ErrorCode.INVALID_ID.getMessage(), "식별자는 비어있거나 음수일 수 없습니다. id: " + id);
    }
}
