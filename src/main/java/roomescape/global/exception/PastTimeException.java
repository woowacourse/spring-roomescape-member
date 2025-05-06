package roomescape.global.exception;

public class PastTimeException extends RuntimeException{
    public PastTimeException(String message) {
        super(message);
    }
}
