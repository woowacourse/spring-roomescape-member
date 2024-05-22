package roomescape.dto.response;

import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponse(Long id, MemberResponse member, LocalDate date,
                                  TimeSlotResponse time, ThemeResponse theme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                reservation.getDate(),
                TimeSlotResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
