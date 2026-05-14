package roomescape.reservationtime.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.exception.InvalidRequestException;

public record ReservationTime(Long id, LocalTime startAt) {

    public void validateFutureDate(LocalDate date) {
        if (LocalDateTime.of(date, startAt).isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("지나간 날짜·시간에는 예약할 수 없습니다.");
        }
    }
}
