package roomescape.reservation.application.dto.request;

import java.time.LocalDate;
import roomescape.reservation.domain.entity.ReservationTheme;
import roomescape.reservation.domain.entity.ReservationTime;
import roomescape.reservation.domain.vo.ReservationDetails;

public record CreateReservationServiceRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationDetails toReservationDetails(ReservationTime reservationTime, ReservationTheme reservationTheme) {
        return ReservationDetails.builder()
                .name(name)
                .date(date)
                .reservationTime(reservationTime)
                .reservationTheme(reservationTheme)
                .build();
    }
}
