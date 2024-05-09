package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        ReservationTimeResponse time,
        RoomThemeResponse theme) {
    public static ReservationResponse fromReservation(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.fromMember(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponse.fromReservationTime(reservation.getTime()),
                RoomThemeResponse.fromRoomTheme(reservation.getTheme()));
    }
}
