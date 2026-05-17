package roomescape.reservationtime.domain;

import roomescape.exception.BusinessRuleException;
import roomescape.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservationTime(Long id, LocalTime startAt) {

    public void validateFutureDate(LocalDate date) {
        if (LocalDateTime.of(date, startAt).isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException(ErrorCode.PAST_DATE_RESERVATION, "지나간 날짜·시간에는 예약할 수 없습니다.");
        }
    }
}
