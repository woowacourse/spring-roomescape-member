package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.exception.InvalidReservationException;

public class Schedule {
    private static final String INVALID_SCHEDULE = "현재보다 이전으로 일정을 설정할 수 없습니다.";

    private final ReservationDate date;
    private final ReservationTime time;

    public Schedule(final ReservationDate date, final ReservationTime time) {
        validate(date, time);
        this.date = date;
        this.time = time;
    }

    private void validate(final ReservationDate date, final ReservationTime time) {
        LocalDateTime value = LocalDateTime.of(LocalDate.parse(date.getValue()), LocalTime.parse(time.getStartAt()));
        if (value.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException(INVALID_SCHEDULE);
        }
    }

    public String getDate() {
        return date.getValue();
    }

    public String getTime() {
        return time.getStartAt();
    }

    public ReservationTime getReservationTime() {
        return time;
    }
}
