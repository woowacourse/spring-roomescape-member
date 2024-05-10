package roomescape.dto;

import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponse(Long id, MemberPreviewResponse member, LocalDate date, ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                new MemberPreviewResponse(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
