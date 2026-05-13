package roomescape.policy;

import roomescape.command.ReservationSaveCommand;
import roomescape.domain.ReservationTime;

import java.time.LocalDateTime;

public class AdminReservationSavePolicy implements ReservationSavePolicy {

    @Override
    public void validate(ReservationSaveCommand command, ReservationTime reservationTime, LocalDateTime now) {
    }
}
