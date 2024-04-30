package roomescape.domain.reservationtime;

public class ReservationTime {

    private final Long id;
    private final ReservationStartAt reservationStartAt;

    public ReservationTime(Long id, ReservationStartAt reservationStartAt) {
        this.id = id;
        this.reservationStartAt = reservationStartAt;
    }

    public boolean isBeforeNow() {
        return reservationStartAt.isBeforeNow();
    }

    public Long getId() {
        return id;
    }

    public ReservationStartAt getStartAt() {
        return reservationStartAt;
    }
}
