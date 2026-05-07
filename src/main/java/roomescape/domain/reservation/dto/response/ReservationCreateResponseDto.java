package roomescape.domain.reservation.dto.response;

import java.time.LocalDate;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

public record ReservationCreateResponseDto(Long id, String name, LocalDate date, Long timeId,
                                           Long themeId) {

    public static ReservationCreateResponseDto from(Reservation reservation) {
        Time time = reservation.getTime();
        Theme theme = reservation.getTheme();

        return new ReservationCreateResponseDto(reservation.getId(), reservation.getName(),
            reservation.getDate(), time.getId(), theme.getId());
    }
}
