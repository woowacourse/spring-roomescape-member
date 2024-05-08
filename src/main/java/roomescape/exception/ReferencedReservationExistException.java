package roomescape.exception;

public class ReferencedReservationExistException extends BadRequestException {
    public ReferencedReservationExistException(String message) {
        super(message);
    }
}
