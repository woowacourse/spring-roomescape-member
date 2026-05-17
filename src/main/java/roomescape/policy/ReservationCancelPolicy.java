package roomescape.policy;

import roomescape.domain.Reservation;

import java.time.LocalDateTime;

@FunctionalInterface
public interface ReservationCancelPolicy {
    void validate(Reservation reservation, LocalDateTime now);
}
