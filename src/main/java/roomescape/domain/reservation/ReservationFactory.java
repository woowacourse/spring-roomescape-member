package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Component;
import roomescape.domain.member.MemberInfo;

@Component
public class ReservationFactory {

    public Reservation createForAdd(LocalDate date, ReservationTime time, ReservationTheme theme, MemberInfo member) {
        validateIsPast(date, time.getStartAt());
        return new Reservation(null, date, time, theme, member);
    }

    private void validateIsPast(LocalDate date, LocalTime time) {
        LocalDateTime input = LocalDateTime.of(date, time);

        if (input.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지난 시간으로는 예약할 수 없습니다.");
        }
    }
}
