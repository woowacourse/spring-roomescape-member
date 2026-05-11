package roomescape.reservation.exception;

public class InvalidReservationTimeException extends RuntimeException {

    public InvalidReservationTimeException(Long timeId) {
        super("존재하지 않는 예약 시간입니다. timeId=" + timeId);
    }
}
