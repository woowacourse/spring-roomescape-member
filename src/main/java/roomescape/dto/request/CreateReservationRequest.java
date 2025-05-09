package roomescape.dto.request;

import java.time.LocalDate;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.domain.User;

public record CreateReservationRequest(
        LocalDate date,
        Long timeId,
        Long themeId
) {

    // TODO: spring validation or RequestBody 없이 파라미터로 바로 입력
    public CreateReservationRequest {
        if (date == null) {
            throw new IllegalArgumentException("날짜는 필수로 입력해야 합니다.");
        }
        if (timeId == null) {
            throw new IllegalArgumentException("시간은 필수로 입력해야 합니다.");
        }
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 필수로 선택해야 합니다.");
        }
    }

    // TODO: 이 메서드를 쓰지말고 엔티티 변환을 다른 곳에서 하는 건 어떨지?
    public Reservation toReservation(User user, ReservationTime reservationTime, ReservationTheme reservationTheme) {
        return new Reservation(user , date, reservationTime, reservationTheme);
    }
}
