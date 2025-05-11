package roomescape.global.exception.notFound;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException(Long id) {
        super(id, "예약");
    }
}
