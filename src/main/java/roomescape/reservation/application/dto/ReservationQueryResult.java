package roomescape.reservation.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationDetail;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.theme.dto.ThemeResponse;

public record ReservationQueryResult(Long id, String name, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date, ThemeResponse theme, ReservationTimeResponse time) {

    public static ReservationQueryResult from(Reservation reservation, ThemeResponse themeResponse,
                                              ReservationTimeResponse timeResponse) {
        return new ReservationQueryResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                themeResponse,
                timeResponse
        );
    }

    public static ReservationQueryResult from(ReservationDetail reservationDetail) {
        return new ReservationQueryResult(
                reservationDetail.reservationId(),
                reservationDetail.username(),
                reservationDetail.date(),
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
