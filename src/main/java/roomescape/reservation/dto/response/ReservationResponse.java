package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.member.dto.response.MemberResponse;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        ReservationTimeResponse time,
        String themeName
) {
    public static ReservationResponse toDto(Reservation reservation) {
        ReservationTimeResponse dto = ReservationTimeResponse.toDto(reservation.getReservationTime());
        return new ReservationResponse(reservation.getId(), new MemberResponse(reservation.getMember().getId(), reservation.getMember().getName()), reservation.getDate(), dto,
                reservation.getTheme().getName());
    }
}
