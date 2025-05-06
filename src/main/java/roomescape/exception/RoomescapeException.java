package roomescape.exception;

public class RoomescapeException extends RuntimeException {

    private static final String ERROR_PREFIX = "[ERROR] ";

    public RoomescapeException(final String message) {
        super(ERROR_PREFIX + message);
    }
}
