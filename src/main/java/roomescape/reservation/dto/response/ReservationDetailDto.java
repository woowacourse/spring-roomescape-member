package roomescape.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.theme.dto.response.ThemeDetailDto;

public record ReservationDetailDto(
        Long id,
        String name,
        LocalDate date,
        LocalTime time,
        ThemeDetailDto theme,
        ReservationStatus status
) {

    public static ReservationDetailDto from(Reservation reservation) {
        return new ReservationDetailDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().getDate(),
                reservation.getTime().getStartAt(),
                ThemeDetailDto.from(reservation.getTheme()),
                reservation.getStatus()
        );
    }

}
