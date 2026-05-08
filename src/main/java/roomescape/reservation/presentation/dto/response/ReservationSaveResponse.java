package roomescape.reservation.presentation.dto.response;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

import java.time.LocalDate;

public record ReservationSaveResponse(
        Long id,
        Long themeId,
        String name,
        LocalDate date,
        TimeInformation time
) {
    public static ReservationSaveResponse from(Reservation reservation) {
        return new ReservationSaveResponse(
                reservation.getId(),
                reservation.getSchedule().getTheme().getId(),
                reservation.getName(),
                reservation.getSchedule().getDate(),
                new TimeInformation(
                        reservation.getSchedule().getTime().getId(),
                        reservation.getSchedule().getTime().getStartAt())
        );
    }
}
