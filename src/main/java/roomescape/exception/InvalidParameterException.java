package roomescape.exception;

public class InvalidParameterException extends BadRequestException{

    public InvalidParameterException() {}

    public InvalidParameterException(String message) {
        super(message);
    }
}
