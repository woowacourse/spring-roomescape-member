package roomescape.domain.reservation.dto;

import java.time.LocalDate;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservationTime.domain.ReservationTime;
import roomescape.domain.theme.domain.Theme;
import roomescape.global.exception.ClientIllegalArgumentException;

public record ReservationAddRequest(LocalDate date, Long timeId, Long themeId, Long memberId) {

    public ReservationAddRequest {
        if (date.isBefore(LocalDate.now())) {
            throw new ClientIllegalArgumentException(date + ": 예약 날짜는 현재 보다 이전일 수 없습니다");
        }
    }

    public Reservation toEntity(ReservationTime reservationTime, Theme theme, Member member) {
        return new Reservation(null, date, reservationTime, theme, member);
    }
}
