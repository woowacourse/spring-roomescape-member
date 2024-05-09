package roomescape.service.dto.response;

import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponse(Long id, MemberNameResponse member, LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), new MemberNameResponse(reservation.getMember().getName()),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getReservationTime()),
                new ThemeResponse(reservation.getTheme()));

    }
}
