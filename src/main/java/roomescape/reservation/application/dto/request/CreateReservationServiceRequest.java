package roomescape.reservation.application.dto.request;

import java.time.LocalDate;
import roomescape.reservation.domain.entity.ReservationTheme;
import roomescape.reservation.domain.entity.ReservationTime;
import roomescape.reservation.domain.dto.ReservationDetails;

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
