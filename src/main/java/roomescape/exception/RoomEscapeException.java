package roomescape.exception;

import roomescape.exception.code.RoomEscapeErrorCode;

public abstract class RoomEscapeException extends RuntimeException {
    private final RoomEscapeErrorCode roomEscapeErrorCode;

    public RoomEscapeException(RoomEscapeErrorCode roomEscapeErrorCode) {
        super(roomEscapeErrorCode.getMessage());
        this.roomEscapeErrorCode = roomEscapeErrorCode;
    }

    public RoomEscapeErrorCode getErrorCode() {
        return roomEscapeErrorCode;
    }
}
