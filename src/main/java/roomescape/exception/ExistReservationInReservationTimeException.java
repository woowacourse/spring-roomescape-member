package roomescape.exception;

public class ExistReservationInReservationTimeException extends ExistReservationException {

    public ExistReservationInReservationTimeException(long id) {
        super(String.format("예약 시간 ID %d에 해당하는 예약이 존재합니다.", id));
    }
}
