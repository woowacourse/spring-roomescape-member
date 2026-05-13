package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationDateTime {

    private final LocalDateTime schedule;

    public ReservationDateTime(LocalDateTime schedule) {
        this.schedule = schedule;
    }

    public ReservationDateTime(LocalDate date, LocalTime time) {
        this.schedule = LocalDateTime.of(date, time);
    }

    public void validateAvailable(LocalDateTime current) {
        if (!isAvailable(current)) {
            throw new IllegalStateException(
                    "미래 시간만 예약할 수 있습니다."
                    + " 예약 희망 시간: " + schedule
                    + " 현재 시간: " + current
            );
        }
    }

    public boolean isAvailable(LocalDateTime current) {
        return schedule.isAfter(current);
    }

    public LocalDate getDate() {
        return schedule.toLocalDate();
    }

    public LocalTime getTime() {
        return schedule.toLocalTime();
    }
}
