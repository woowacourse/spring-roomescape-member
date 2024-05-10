package roomescape.exception;

public class WrongStateException extends RuntimeException{

    public WrongStateException () {}

    public WrongStateException(String message) {
        super(message);
    }
}
