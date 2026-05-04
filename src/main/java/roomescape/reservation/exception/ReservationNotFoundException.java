package roomescape.reservation.exception;

public class ReservationNotFoundException extends RuntimeException {
    private final long id;

    public ReservationNotFoundException(long id) {
        super("예약이 존재하지 않습니다. id=" + id);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
