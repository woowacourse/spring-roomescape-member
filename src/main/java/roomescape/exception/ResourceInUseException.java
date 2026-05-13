package roomescape.exception;

public class ResourceInUseException extends ApiException {
    public ResourceInUseException(String message) {
        super(message);
    }
}