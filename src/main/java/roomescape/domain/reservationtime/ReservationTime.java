package roomescape.domain.reservationtime;

import java.time.LocalDateTime;

public class ReservationTime {

    private final Long id;
    private final ReservationStartAt reservationStartAt;

    public ReservationTime(Long id, ReservationStartAt reservationStartAt) {
        this.id = id;
        this.reservationStartAt = reservationStartAt;
    }

    public boolean isBefore(LocalDateTime dateTime) {
        return reservationStartAt.isBefore(dateTime);
    }

    public Long getId() {
        return id;
    }

    public ReservationStartAt getStartAt() {
        return reservationStartAt;
    }
}
