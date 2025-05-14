package roomescape.common.exception;

public class DataConflictException extends CustomException {

    public DataConflictException(String message) {
        super(message, ErrorCode.CONFLICT);
    }

    public DataConflictException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
