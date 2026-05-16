package roomescape.reservation.exception;

public class PastReservationException extends RuntimeException {
    private PastReservationException(String message) {
        super(message);
    }

    public static PastReservationException pastDate() {
        return new PastReservationException("과거 날짜는 예약이 불가합니다.");
    }

    public static PastReservationException pastTime() {
        return new PastReservationException("과거 시간은 예약이 불가합니다.");
    }

    public static PastReservationException pastCancel() {
        return new PastReservationException("이미 지난 예약은 취소할 수 없습니다.");
    }
}
