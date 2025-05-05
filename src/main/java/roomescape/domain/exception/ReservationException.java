package roomescape.domain.exception;

public class ReservationException extends RuntimeException {

    public ReservationException(String message) {
        super(message);
    }

    public static final class InvalidReservationTimeException extends ReservationException {

        public InvalidReservationTimeException(String message) {
            super(message);
        }
    }

    public static final class ReservationThemeInUseException extends ReservationException {

        public ReservationThemeInUseException(String message) {
            super(message);
        }
    }

    public static final class ReservationTimeInUseException extends ReservationException {

        public ReservationTimeInUseException(String message) {
            super(message);
        }
    }
}
