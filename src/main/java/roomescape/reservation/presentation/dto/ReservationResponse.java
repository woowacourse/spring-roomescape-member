package roomescape.reservation.presentation.dto;

import java.time.LocalDate;
import lombok.Builder;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.presentation.dto.ThemeResponse;
import roomescape.time.presentation.dto.ReservationTimeResponse;

@Builder
public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .name(reservation.getName())
                .date(reservation.getDate())
                .time(ReservationTimeResponse.from(reservation.getTime()))
                .theme(ThemeResponse.from(reservation.getTheme()))
                .build();
    }
}
