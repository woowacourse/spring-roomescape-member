package roomescape.reservation.application.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.application.query.ReservationDetail;
import roomescape.reservationtime.application.dto.ReservationTimeResult;
import roomescape.theme.application.dto.ThemeResult;

public record ReservationQueryResult(
        Long id,
        String name,
        LocalDate date,
        ThemeResult theme,
        ReservationTimeResult time
) {

    public static ReservationQueryResult from(Reservation reservation, ThemeResult themeResult,
                                              ReservationTimeResult timeResult) {
        return new ReservationQueryResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                themeResult,
                timeResult
        );
    }

    public static ReservationQueryResult from(ReservationDetail reservationDetail) {
        return new ReservationQueryResult(
                reservationDetail.reservationId(),
                reservationDetail.username(),
                reservationDetail.date(),
                ThemeResult.from(
                        reservationDetail.themeId(),
                        reservationDetail.themeName(),
                        reservationDetail.themeDescription(),
                        reservationDetail.thumbnailImgUrl()
                ),
                ReservationTimeResult.from(
                        reservationDetail.timeId(),
                        reservationDetail.startAt()
                )
        );
    }
}
