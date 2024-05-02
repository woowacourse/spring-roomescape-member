package roomescape.exception;

public class NotExistReservationException extends NotExistException {

    public NotExistReservationException(final long id) {
        super(String.format("예약 ID %d에 해당하는 값이 없습니다", id));
    }
}
