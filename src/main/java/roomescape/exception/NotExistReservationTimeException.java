package roomescape.exception;

public class NotExistReservationTimeException extends NotExistException {

    public NotExistReservationTimeException(final long id) {
        super(String.format("예약 시간 ID %d에 해당하는 값이 없습니다", id));
    }
}
