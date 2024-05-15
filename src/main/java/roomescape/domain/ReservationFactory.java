package roomescape.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationFactory {

    private final Clock clock;

    public ReservationFactory(Clock clock) {
        this.clock = clock;
    }

    public Reservation create(String date, Member member, ReservationTime time, Theme theme) {
        LocalDate reservationDate = LocalDate.parse(date);
        LocalTime reservationStartAt = time.getStartAt();
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationStartAt);
        LocalDateTime nowDateTime = LocalDateTime.now(clock);

        if (reservationDateTime.isBefore(nowDateTime)) {
            throw new IllegalStateException("이미 지나간 시간에 대한 예약을 할 수 없습니다.");
        }

        return new Reservation(LocalDate.parse(date), member, time, theme);
    }
}
