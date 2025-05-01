package roomescape.domain;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReservationRegistrationPolicy {

    public void validate(Reservation candidate, List<Reservation> existingReservations) {
        if (candidate.isPast()) {
            throw new IllegalArgumentException("과거 일시로 예약할 수 없습니다.");
        }
        if (existingReservations.stream().anyMatch(r -> r.isDuplicated(candidate))) {
            throw new IllegalArgumentException("이미 예약된 일시입니다.");
        }
    }
}
