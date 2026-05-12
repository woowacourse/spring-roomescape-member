package roomescape.time.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.reservation.domain.exception.IllegalReservationDateTimeException;

@Getter
@Builder
@EqualsAndHashCode
public class ReservationTime {

    private Long id;
    private LocalTime startAt;

    public ReservationTime withId(Long id) {
        return ReservationTime.builder()
                .id(id)
                .startAt(this.startAt)
                .build();
    }

    public void checkRegisterable(LocalDate date, Clock clock) {
        LocalDateTime time = LocalDateTime.of(date, startAt);
        if (time.isBefore(LocalDateTime.now(clock))) {
            throw new IllegalReservationDateTimeException("과거의 시간으로 예약을 할 수 없습니다.");
        }
    }

    public void checkChangeable(LocalDate date, Clock clock) {
        LocalDateTime newReservationDateTime = LocalDateTime.of(date, startAt);
        if (newReservationDateTime.isBefore(LocalDateTime.now(clock))) {
            throw new IllegalReservationDateTimeException("과거의 시간으로 예약을 변경할 수 없습니다.");
        }
    }
}
