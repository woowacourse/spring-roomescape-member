package roomescape.reservation.dto;

import java.time.format.DateTimeFormatter;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDetail;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.theme.dto.ThemeResponse;

public record ReservationResponse(Long id, String name, String date, ThemeResponse theme, ReservationTimeResponse time) {

    public static ReservationResponse from(Reservation reservation, ThemeResponse themeResponse,
                                           ReservationTimeResponse timeResponse) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                themeResponse,
                timeResponse
        );
    }

    public static ReservationResponse from(ReservationDetail reservationDetail) {
        return new ReservationResponse(
                reservationDetail.reservationId(),
                reservationDetail.username(),
                reservationDetail.date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                ThemeResponse.from(
                        reservationDetail.themeId(),
                        reservationDetail.themeName(),
                        reservationDetail.themeDescription(),
                        reservationDetail.thumbnailImgUrl()
                ),
                ReservationTimeResponse.from(
                        reservationDetail.timeId(),
                        reservationDetail.startAt()
                )
        );
    }
}
