package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.InvalidRequestValueException;

public record ReservationWithTime(String name, LocalDate date, ReservationTime time, Theme reservationTheme) {
    private static final String CANNOT_UPDATE_PAST_RESERVATION = "이미 지난 예약은 수정할 수 없습니다.";

    public void validDateReservationDateTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationTime = LocalDateTime.of(date, time.startAt());

        if(reservationTime.isBefore(now)) {
            throw new InvalidRequestValueException(CANNOT_UPDATE_PAST_RESERVATION);
        }
    }
}
