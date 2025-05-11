package roomescape.domain.reservation;

import java.time.LocalDate;
import roomescape.common.exception.reservation.InvalidReservationException;

public class ReservationDate {

    private final LocalDate date;

    public ReservationDate(LocalDate date) {
        this.date = date;
    }

    public boolean isCurrentDay(LocalDate currentDate) {
        return this.date.equals(currentDate);
    }

    public void validateDate(LocalDate currentDate) {
        if (date.isBefore(currentDate)) {
            throw new InvalidReservationException("현재 날짜 기준으로 과거의 날짜는 예약할 수 없습니다.");
        }
    }

    public LocalDate getDate() {
        return date;
    }
}
