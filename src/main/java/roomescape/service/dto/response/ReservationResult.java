package roomescape.service.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResult(long id,
                                MemberResult member,
                                LocalDate date,
                                ReservationTimeResult time,
                                RoomThemeResult theme) {

    public static ReservationResult from(Reservation reservation) {
        return new ReservationResult(reservation.getId(),
                MemberResult.from(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResult.from(reservation.getTime()),
                RoomThemeResult.from(reservation.getTheme()));
    }
}
