package roomescape.exception;

public class NotFoundException extends ApiException {
    public NotFoundException(String code, String message) {
        super(code, message);
    }
}
