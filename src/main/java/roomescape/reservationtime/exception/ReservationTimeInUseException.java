package roomescape.reservationtime.exception;

public class ReservationTimeInUseException extends RuntimeException {

    public static final String MESSAGE = "예약이 존재하는 시간은 삭제할 수 없습니다.";

    public ReservationTimeInUseException(Long id) {
        super(MESSAGE + " id=" + id);
    }

}
