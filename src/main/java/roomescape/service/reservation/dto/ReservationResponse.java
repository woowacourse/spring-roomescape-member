package roomescape.service.reservation.dto;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;
import roomescape.service.member.dto.MemberResponse;
import roomescape.service.reservationtime.dto.ReservationTimeResponse;

public record ReservationResponse(Long id, MemberResponse member, LocalDate date, ReservationTimeResponse time, Theme theme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                reservation.getTheme()
        );
    }
}
