package roomescape.domain.reservation;

import java.time.LocalDate;
import roomescape.common.exception.reservation.InvalidReservationException;

public record ReservationDate(LocalDate date) {

    public boolean isCurrentDay(final LocalDate currentDate) {
        return date.equals(currentDate);
    }

    public void validateDate(final LocalDate currentDate) {
        if (date.isBefore(currentDate)) {
            throw new InvalidReservationException("현재 날짜 기준으로 과거의 날짜는 예약할 수 없습니다.");
        }
    }
}

