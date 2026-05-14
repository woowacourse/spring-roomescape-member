package roomescape.policy;

import roomescape.command.ReservationSaveCommand;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDateTime;

@FunctionalInterface
public interface ReservationSavePolicy {
    void validate(Reservation reservation, LocalDateTime now);
}
