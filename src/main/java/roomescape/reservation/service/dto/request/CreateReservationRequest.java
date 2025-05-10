package roomescape.reservation.service.dto.request;

import roomescape.reservation.entity.Reservation;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;

public record CreateReservationRequest(
        LocalDate date,
        Long memberId,
        Long timeId,
        Long themeId
) {
    public Reservation toEntity(ReservationTime timeEntity) {
        return Reservation.create(memberId, date, timeEntity, themeId);
    }
}
