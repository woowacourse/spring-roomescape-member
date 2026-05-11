package roomescape.reservation.application.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.application.query.ReservationDetail;
import roomescape.reservationtime.application.dto.ReservationTimeQueryResult;
import roomescape.theme.application.dto.ThemeResult;

public record ReservationQueryResult(
        Long id,
        String name,
        LocalDate date,
        ThemeResult theme,
        ReservationTimeQueryResult time
) {

    public static ReservationQueryResult from(Reservation reservation, ThemeResult themeResult,
                                              ReservationTimeQueryResult timeQueryResult) {
        return new ReservationQueryResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                themeResult,
                timeQueryResult
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
                ReservationTimeQueryResult.from(
                        reservationDetail.timeId(),
                        reservationDetail.startAt()
                )
        );
    }
}
