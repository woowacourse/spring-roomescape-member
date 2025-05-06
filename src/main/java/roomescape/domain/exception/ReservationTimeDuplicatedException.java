package roomescape.domain.exception;

public class ReservationTimeDuplicatedException extends IllegalArgumentException {

    private static final String message = "[ERROR] 해당 시간이 이미 존재합니다.";

    public ReservationTimeDuplicatedException() {
        super(message);
    }
}
