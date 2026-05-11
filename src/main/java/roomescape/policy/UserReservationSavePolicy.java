package roomescape.policy;

import roomescape.command.ReservationSaveCommand;

import java.time.LocalDate;

public class UserReservationSavePolicy implements ReservationSavePolicy {

    private final LocalDate today;

    public UserReservationSavePolicy(LocalDate today) {
        this.today = today;
    }

    @Override
    public void validate(ReservationSaveCommand command) {
        if (command.date().isBefore(today)) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
    }
}
