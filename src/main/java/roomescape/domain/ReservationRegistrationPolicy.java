package roomescape.domain;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.domain.exception.ImpossibleReservationException;

@Component
public class ReservationRegistrationPolicy {

    public void validate(Reservation candidate, List<Reservation> existingReservations) {
        if (candidate.isPast()) {
            throw new ImpossibleReservationException("과거 일시로 예약할 수 없습니다.");
        }
        if (existingReservations.stream().anyMatch(r -> r.isDuplicated(candidate))) {
            throw new ImpossibleReservationException("이미 예약된 일시입니다.");
        }
    }
}
