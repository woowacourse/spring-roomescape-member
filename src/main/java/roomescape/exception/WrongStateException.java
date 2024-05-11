package roomescape.exception;

public class WrongStateException extends BadRequestException{

    public WrongStateException () {}

    public WrongStateException(String message) {
        super(message);
    }
}
