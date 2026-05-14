package roomescape.policy;

import roomescape.domain.Reservation;

import java.time.LocalDateTime;

public class AdminReservationSavePolicy implements ReservationSavePolicy {

    @Override
    public void validate(Reservation reservation, LocalDateTime now) {
    }
}
