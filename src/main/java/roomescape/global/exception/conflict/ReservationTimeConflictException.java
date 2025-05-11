package roomescape.global.exception.conflict;

public class ReservationTimeConflictException extends ConflictException {
    public ReservationTimeConflictException() {
        super("시간");
    }
}
