package roomescape.time.exception;

public class ReservationTimeConflictException extends RuntimeException {
    private final Long id;

    public ReservationTimeConflictException(Long id) {
        super("존재하는 예약 시간입니다. id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
