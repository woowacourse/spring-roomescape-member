package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class ReservationFactory {

    public Reservation createForAdd(String name, LocalDate date, ReservationTime time, ReservationTheme theme) {
        validateIsPast(date, time.getStartAt());
        return new Reservation(null, name, date, time, theme);
    }

    private void validateIsPast(LocalDate date, LocalTime time) {
        LocalDateTime input = LocalDateTime.of(date, time);

        if (input.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지난 시간으로는 예약할 수 없습니다.");
        }
    }
}
