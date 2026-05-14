package roomescape.global.exception.customException;

import roomescape.global.exception.ErrorCode;

public class EntityNotFoundException extends RoomEscapeException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
