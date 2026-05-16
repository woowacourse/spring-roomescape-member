package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.exception.InvalidRequestValueException;

public record ReservationWithTime(long id, String name, LocalDate date, ReservationTime reservationTime, long themeId) {
    private static final String CANNOT_UPDATE_SAME_VALUE = "기존 정보와 동일하여 수정할 내용이 없습니다.";

    public void validateEqualValue(String name, LocalDate date, long timeId, long themeId) {
        if(isEqualValue(name, date, timeId, themeId)) {
            throw new InvalidRequestValueException(CANNOT_UPDATE_SAME_VALUE);
        }
    }

    public void validDateReservationPastDateTime(String errorMessage) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationTime = LocalDateTime.of(date, reservationTime().startAt());

        if(reservationTime.isBefore(now)) {
            throw new InvalidRequestValueException(errorMessage);
        }
    }

    private boolean isEqualValue(String name, LocalDate date, long timeId, long themeId) {
        return (
                this.name.equals(name) && this.date.equals(date) && reservationTime().id() == timeId && this.themeId == themeId
        );
    }
}
