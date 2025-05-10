package roomescape.reservation.domain.dto;

import java.time.LocalDate;
import lombok.Builder;
import roomescape.reservation.domain.entity.Reservation;
import roomescape.reservation.domain.entity.ReservationTheme;
import roomescape.reservation.domain.entity.ReservationTime;

@Builder
public record ReservationDetails(
        String memberName,
        Long memberId,
        LocalDate date,
        ReservationTime reservationTime,
        ReservationTheme reservationTheme
) {

    public Reservation toReservation() {
        return Reservation.builder()
                .name(memberName)
                .memberId(memberId)
                .date(date)
                .time(reservationTime)
                .theme(reservationTheme)
                .build();
    }
}
