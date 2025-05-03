package roomescape.exception;

public class ReservationDuplicateException extends RuntimeException {

    public ReservationDuplicateException(ExceptionCause exceptionCause) {
        super(exceptionCause.getMessage());
    }
}
