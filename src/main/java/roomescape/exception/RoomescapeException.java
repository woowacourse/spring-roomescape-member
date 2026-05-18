package roomescape.exception;

public class RoomescapeException extends RuntimeException {

    private final ErrorReason errorReason;

    public RoomescapeException(ErrorReason errorReason) {
        super(errorReason.getMessage());
        this.errorReason = errorReason;
    }

    public ErrorReason getErrorReason() {
        return errorReason;
    }
}
