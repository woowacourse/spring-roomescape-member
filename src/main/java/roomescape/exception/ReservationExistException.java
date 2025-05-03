package roomescape.exception;

public class ReservationExistException extends RuntimeException {

    public ReservationExistException(ExceptionCause exceptionCause) {
        super(exceptionCause.getMessage());
    }
}
