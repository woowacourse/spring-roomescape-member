package roomescape.exception;

public class RoomEscapeException extends RuntimeException {

    public RoomEscapeException() {
        super();
    }

    public RoomEscapeException(String message) {
        super(message);
    }

    public RoomEscapeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoomEscapeException(Throwable cause) {
        super(cause);
    }
}
