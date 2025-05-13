package roomescape.domain.reservation.application.dto.request;

import java.time.LocalDate;
import roomescape.domain.reservation.model.entity.ReservationTheme;
import roomescape.domain.reservation.model.entity.ReservationTime;
import roomescape.domain.reservation.model.dto.ReservationDetails;

public record CreateReservationServiceRequest(
        String memberName,
        Long memberId,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationDetails toReservationDetails(ReservationTime reservationTime, ReservationTheme reservationTheme) {
        return ReservationDetails.builder()
                .memberName(memberName)
                .memberId(memberId)
                .date(date)
                .reservationTime(reservationTime)
                .reservationTheme(reservationTheme)
                .build();
    }
}
