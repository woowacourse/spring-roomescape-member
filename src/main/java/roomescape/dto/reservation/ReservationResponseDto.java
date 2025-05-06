package roomescape.dto.reservation;

import java.time.LocalDate;
import roomescape.dto.reservationtime.ReservationTimeResponseDto;
import roomescape.dto.theme.ThemeResponseDto;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

public record ReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {
    public static ReservationResponseDto from(Reservation reservationInfo) {
        ReservationTime timeInfo = reservationInfo.getTime();
        Theme themeInfo = reservationInfo.getTheme();

        return new ReservationResponseDto(
                reservationInfo.getId(),
                reservationInfo.getName(),
                reservationInfo.getDate(),
                new ReservationTimeResponseDto(timeInfo.getId(), timeInfo.getStartAt()),
                new ThemeResponseDto(themeInfo.getId(), themeInfo.getName(), themeInfo.getDescription(),
                        themeInfo.getThumbnail())
        );
    }
}
