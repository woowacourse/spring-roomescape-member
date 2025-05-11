package roomescape.presentation.dto.response;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getReservationTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
