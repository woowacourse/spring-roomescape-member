package roomescape.reservationtime.exception;

public class ReservationTimeDuplicatedException extends RuntimeException {

    public ReservationTimeDuplicatedException() {
        super("이미 존재하는 예약 시간입니다.");
    }
}
