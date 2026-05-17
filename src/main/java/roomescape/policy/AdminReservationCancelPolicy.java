package roomescape.policy;

import roomescape.domain.Reservation;

import java.time.LocalDateTime;

public class AdminReservationCancelPolicy implements ReservationCancelPolicy {

    @Override
    public void validate(Reservation reservation, LocalDateTime now) {
    }
}
