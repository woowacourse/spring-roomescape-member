package roomescape.policy;

import roomescape.command.ReservationSaveCommand;

@FunctionalInterface
public interface ReservationSavePolicy {
    void validate(ReservationSaveCommand command);
}
