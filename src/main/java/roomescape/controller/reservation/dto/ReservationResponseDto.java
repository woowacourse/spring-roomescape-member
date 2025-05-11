package roomescape.controller.reservation.dto;

import java.time.LocalDate;
import roomescape.controller.reservationTime.dto.ReservationTimeResponseDto;
import roomescape.controller.theme.dto.ThemeResponseDto;
import roomescape.model.Member;
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
        Member member = reservationInfo.getMember();
        return new ReservationResponseDto(
                reservationInfo.getId(),
                member.getName(),
                reservationInfo.getDate(),
                new ReservationTimeResponseDto(timeInfo.getId(), timeInfo.getStartAt()),
                new ThemeResponseDto(themeInfo.getId(), themeInfo.getName(), themeInfo.getDescription(),
                        themeInfo.getThumbnail())
        );
    }
}
