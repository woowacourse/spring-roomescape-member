package roomescape.exception;

public class EmptyParameterException extends BadRequestException {
    public EmptyParameterException(String message) {
        super(message);
    }
}
