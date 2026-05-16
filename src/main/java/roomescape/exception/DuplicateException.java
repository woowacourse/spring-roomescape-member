package roomescape.exception;

public class DuplicateException extends ApiException {
    public DuplicateException(ErrorCode code, String message) {
        super(code, message);
    }
}
