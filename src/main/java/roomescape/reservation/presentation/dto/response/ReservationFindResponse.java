package roomescape.reservation.presentation.dto.response;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

import java.time.LocalDate;
import java.util.List;

public record ReservationFindResponse(
        Long id,
        String name,
        LocalDate date,
        ThemeFindResponse theme,
        TimeInformation time
) {
    public static List<ReservationFindResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> new ReservationFindResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getSchedule().getDate(),
                        new ThemeFindResponse(
                                reservation.getSchedule().getTheme().getId(),
                                reservation.getSchedule().getTheme().getName(),
                                reservation.getSchedule().getTheme().getDescription(),
                                reservation.getSchedule().getTheme().getThumbnailUrl()
                        ),
                        new TimeInformation(
                                reservation.getSchedule().getTime().getId(),
                                reservation.getSchedule().getTime().getStartAt()
                        )
                        ))
                .toList();
    }
}
