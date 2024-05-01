package roomescape.dto.request;

import java.time.LocalDate;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

public record ReservationRequest(String name, String date, Long timeId, Long themeId) {
    public Reservation toReservation(ReservationTime reservationTime, RoomTheme roomTheme) {
        return new Reservation(new Name(name), LocalDate.parse(date), reservationTime, roomTheme);
    }
}
