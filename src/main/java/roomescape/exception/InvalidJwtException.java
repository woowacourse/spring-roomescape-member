package roomescape.exception;

public class InvalidJwtException extends RuntimeException{

    public InvalidJwtException(ExceptionCause exceptionCause) {
        super(exceptionCause.getMessage());
    }
}
