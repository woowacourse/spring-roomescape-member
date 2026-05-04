package roomescape.request;

import roomescape.command.ReservationSaveCommand;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId) {

    public ReservationSaveCommand toSaveCommand() {
        return new ReservationSaveCommand(name, date, timeId);
    }
}
