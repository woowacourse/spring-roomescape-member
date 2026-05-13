package roomescape.reservation.application;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.exception.PastReservationException;

@Component
@RequiredArgsConstructor
public class ReservationSchedulePolicy {

    private final Clock clock;

    public void validateReservable(LocalDate date, LocalTime startAt) {
        if (!canReserve(date, startAt)) {
            throw new PastReservationException("지나간 날짜/시간으로는 예약할 수 없습니다.");
        }
    }

    public boolean canReserve(LocalDate date, LocalTime startAt) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, startAt);
        LocalDateTime now = LocalDateTime.now(clock);
        return reservationDateTime.isAfter(now);
    }
}
