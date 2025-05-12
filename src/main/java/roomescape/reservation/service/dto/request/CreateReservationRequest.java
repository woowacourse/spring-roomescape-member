package roomescape.reservation.service.dto.request;

import roomescape.auth.service.dto.response.LoginMember;
import roomescape.reservation.entity.Reservation;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;

public record CreateReservationRequest(
        LocalDate date,
        LoginMember loginMember,
        Long timeId,
        Long themeId
) {
    public Reservation toEntity(ReservationTime timeEntity) {
        return new Reservation(0L, loginMember().id(), date, timeEntity, themeId);
    }
}
