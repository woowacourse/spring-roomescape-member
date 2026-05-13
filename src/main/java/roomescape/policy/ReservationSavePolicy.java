package roomescape.policy;

import roomescape.command.ReservationSaveCommand;
import roomescape.domain.ReservationTime;

import java.time.LocalDateTime;

@FunctionalInterface
public interface ReservationSavePolicy {
    void validate(ReservationSaveCommand command, ReservationTime reservationTime, LocalDateTime now);
}
