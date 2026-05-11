package roomescape.domain.reservation;

import java.time.LocalDate;

public record Reservation(long id, String name, LocalDate date, long timeId, long themeId) {
    public boolean isEqualValue(ReservationCommand reservationCommand) {
        return (
                reservationCommand.name().equals(name) &&
                reservationCommand.date().equals(date) &&
                reservationCommand.timeId() == timeId &&
                reservationCommand.themeId() == themeId
        );
    }
}
