package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ThemeResponseDto;
import roomescape.time.dto.ReservationTimeResponseDto;

import java.time.LocalDate;

public record ReservationResponseDto(long id, String name, LocalDate date, ReservationTimeResponseDto time,
                                     ThemeResponseDto theme) {

    public ReservationResponseDto(final Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(),
                new ReservationTimeResponseDto(reservation.getTime()),
                new ThemeResponseDto(reservation.getTheme()));
    }
}
