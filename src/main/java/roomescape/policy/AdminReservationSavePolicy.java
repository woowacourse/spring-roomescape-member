package roomescape.policy;

import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;

import java.time.LocalDateTime;

@Component
public class AdminReservationSavePolicy implements ReservationSavePolicy {

    @Override
    public void validate(Reservation reservation, LocalDateTime now) {
    }
}
