package roomescape.reservation.application.dto.info;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.application.dto.ThemeDto;
import roomescape.time.application.dto.info.TimeDto;

public record ReservationDto(
        long id,
        long memberId,
        ThemeDto theme,
        LocalDate date,
        TimeDto time
) {
    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getMemberId(),
                ThemeDto.from(reservation.getTheme()),
                reservation.getReservationDate(),
                TimeDto.from(reservation.getReservationTime())
        );
    }

    public static List<ReservationDto> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationDto::from)
                .toList();
    }
}
