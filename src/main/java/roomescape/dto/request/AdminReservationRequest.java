package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

public record AdminReservationRequest(
        @NotNull(message = "예약 날짜 입력이 존재하지 않습니다.") LocalDate date,
        @NotNull(message = "사용자 입력이 존재하지 않습니다.") Long memberId,
        @NotNull(message = "예약 시간 입력이 존재하지 않습니다.") Long timeId,
        @NotNull(message = "테마 입력이 존재하지 않습니다.") Long themeId
) implements ReservationRequest {
    @Override
    public Reservation toReservation(Member member,
                                     ReservationTime reservationTime,
                                     RoomTheme roomTheme) {
        return new Reservation(date, member, reservationTime, roomTheme);
    }
}
