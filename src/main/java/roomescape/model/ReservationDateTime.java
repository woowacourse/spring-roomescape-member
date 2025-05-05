package roomescape.model;

import java.time.LocalDate;

public class ReservationDateTime {
    private final LocalDate date;
    private final ReservationTime time;

    public ReservationDateTime(LocalDate date, ReservationTime time) {
        validateNotNull(date, time);
        this.date = date;
        this.time = time;
    }

    private void validateNotNull(final LocalDate date, final ReservationTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("날짜와 시간이 모두 필요합니다.");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }


}
