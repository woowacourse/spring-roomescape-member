package roomescape.reservation.exception;

public class ReservationNotFoundException extends RuntimeException {
    private final Long id;

    public ReservationNotFoundException(Long id) {
        super("예약이 존재하지 않습니다. id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
