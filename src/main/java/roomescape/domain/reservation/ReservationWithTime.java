package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.exception.InvalidRequestValueException;

public record ReservationWithTime(long id, String name, LocalDate date, ReservationTime reservationTime, long themeId) {
    private static final String CANNOT_UPDATE_SAME_VALUE = "기존 정보와 동일하여 수정할 내용이 없습니다.";

    public void validateEqualValue(ReservationCommand reservationCommand) {
        if(isEqualValue(reservationCommand)) {
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

    private boolean isEqualValue(ReservationCommand reservationCommand) {
        return (
                reservationCommand.name().equals(name) &&
                        reservationCommand.date().equals(date) &&
                        reservationCommand.timeId() == reservationTime().id() &&
                        reservationCommand.themeId() == themeId
        );
    }
}
