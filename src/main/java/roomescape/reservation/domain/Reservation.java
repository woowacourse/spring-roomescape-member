package roomescape.reservation.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.reservation.domain.exception.IllegalReservationDateTimeException;
import roomescape.reservation.domain.exception.IllegalStateReservationException;
import roomescape.reservation.domain.exception.UnauthorizedReservationChangeException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;
    private Status status;
    private LocalDateTime deletedAt;

    public Reservation withId(Long id) {
        return Reservation.builder()
                .id(id)
                .name(this.name)
                .date(this.date)
                .time(this.time)
                .theme(this.theme)
                .status(Status.ACTIVE)
                .build();
    }

    public void checkChangeable(String username, Clock clock) {
        if (!this.name.equals(username)) {
            throw new UnauthorizedReservationChangeException("예약 변경 권한이 없습니다.");
        }
        if (status == Status.CANCELED) {
            throw new IllegalStateReservationException("이미 취소된 예약은 변경할 수 없습니다.");
        }
        if (LocalDateTime.of(date, time.getStartAt()).isBefore(LocalDateTime.now(clock))) {
            throw new IllegalReservationDateTimeException("이미 지난 예약은 변경할 수 없습니다.");
        }
    }

    public Reservation changeTime(LocalDate date, ReservationTime time, Theme theme) {
        return Reservation.builder()
                .id(id)
                .name(this.name)
                .date(date)
                .time(time)
                .theme(theme)
                .status(Status.ACTIVE)
                .build();
    }

    public Reservation cancel(Clock clock) {
        return Reservation.builder()
                .id(id)
                .name(name)
                .date(date)
                .time(time)
                .theme(theme)
                .status(Status.CANCELED)
                .deletedAt(LocalDateTime.now(clock))
                .build();
    }
}
