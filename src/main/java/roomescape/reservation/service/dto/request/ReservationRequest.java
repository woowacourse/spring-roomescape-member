package roomescape.reservation.service.dto.request;

import jakarta.validation.constraints.NotNull;
import roomescape.auth.service.dto.LoginMember;
import roomescape.reservation.entity.Reservation;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;

public record ReservationRequest(
        @NotNull(message = "날짜가 입력되지 않았습니다.")
        LocalDate date,
        @NotNull(message = "시간이 선택되지 않았습니다.")
        Long timeId,
        @NotNull(message = "테마가 선택되지 않았습니다.")
        Long themeId
) {
    public Reservation toEntity(LoginMember loginMember, ReservationTime timeEntity) {
        return new Reservation(null, loginMember.id(), date, timeEntity, themeId);
    }
}
