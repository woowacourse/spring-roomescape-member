package roomescape.command;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.ReservationTime;
import roomescape.exception.UnprocessableException;
import roomescape.exception.code.UnprocessableCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationEditCommand(
        @NotNull LocalDate date,
        @NotNull Long timeId) {
    public void validateNow(ReservationTime reservationTime, LocalDateTime now) {
        if (date().isBefore(now.toLocalDate())) {
            throw new UnprocessableException(UnprocessableCode.RESERVATION_PAST_DATE);
        }

        LocalDateTime editedDateTime = date.atTime(reservationTime.startAt());
        if (editedDateTime.isBefore(now)) {
            throw new UnprocessableException(UnprocessableCode.RESERVATION_PAST_TIME);
        }
    }
}
