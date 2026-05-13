package roomescape.reservationtime.exception;

public class ReservationTimeInUseException extends RuntimeException {

    public ReservationTimeInUseException(Long id) {
        super("예약이 존재하는 시간은 삭제할 수 없습니다. id=" + id);
    }

}
