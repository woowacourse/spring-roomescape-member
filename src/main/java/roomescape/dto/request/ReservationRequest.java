package roomescape.dto.request;

import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

public interface ReservationRequest {
    Reservation toReservation(Member member,
                              ReservationTime reservationTime,
                              RoomTheme roomTheme);

    LocalDate date();

    Long timeId();

    Long themeId();
}
