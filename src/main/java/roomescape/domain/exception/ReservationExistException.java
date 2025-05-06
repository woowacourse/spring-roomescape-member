package roomescape.domain.exception;

public class ReservationExistException extends IllegalArgumentException {

    private static final String message = "[ERROR] 연관된 예약이 존재하기 때문에 삭제할 수 없습니다.";

    public ReservationExistException() {
        super(message);
    }
}
