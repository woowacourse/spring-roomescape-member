package roomescape.domain.reservation.model;

import java.time.LocalDate;
import java.util.Objects;

public class ReservationDate {

    private final LocalDate date;

    public ReservationDate(LocalDate date) {
        this.date = date;
    }

    public boolean isCurrentDay(LocalDate currentDate) {
        return currentDate.equals(date);
    }

    public void validateDate(LocalDate currentDate) {
        if (date.isBefore(currentDate)) {
            throw new IllegalArgumentException("현재 날짜 기준으로 과거의 날짜는 예약할 수 없습니다.");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationDate that = (ReservationDate) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }
}
