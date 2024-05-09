package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationCreateRequest(
        Long id,

        @NotNull(message = "[ERROR] 날짜는 비어있을 수 없습니다.")
        LocalDate date,

        @NotNull(message = "[ERROR] 예약 시간 Id는 비어있을 수 없습니다.")
        Long timeId,

        @NotNull(message = "[ERROR] 테마 Id는 비어있을 수 없습니다.")
        Long themeId
) {

    public static Reservation toReservation(final String memberName, final LocalDate date, final ReservationTime time, final Theme theme,
                                            final Member member) {
        return new Reservation(null, memberName, date, time, theme, member);
    }
}
