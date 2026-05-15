package roomescape.exception;

public class DuplicateException extends ApiException {
    public DuplicateException(String code, String message) {
        super(code, message);
    }
}
