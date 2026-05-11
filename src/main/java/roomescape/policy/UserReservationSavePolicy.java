package roomescape.policy;

import org.springframework.stereotype.Component;
import roomescape.command.ReservationSaveCommand;

import java.time.Clock;
import java.time.LocalDate;

@Component
public class UserReservationSavePolicy implements ReservationSavePolicy {

    private final Clock clock;

    public UserReservationSavePolicy(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void validate(ReservationSaveCommand command) {
        if (command.date().isBefore(LocalDate.now(clock))) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
    }
}
