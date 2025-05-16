package roomescape.global.exception.notFound;

public class ReservationTimeNotFoundException extends NotFoundException {
    public ReservationTimeNotFoundException(Long id) {
        super(id, "예약 시간");
    }
}
