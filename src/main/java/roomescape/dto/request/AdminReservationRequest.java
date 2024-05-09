package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record AdminReservationRequest(
        @NotNull(message = "memberId를 입력해주세요.")
        Long memberId,

        @NotNull(message = "date를 입력해주세요.")
        LocalDate date,

        @NotNull(message = "timeId를 입력해주세요.")
        Long timeId,

        @NotNull(message = "themeId를 입력해주세요.")
        Long themeId) {
    public Reservation toReservation(Member member, ReservationTime time, Theme theme) {
        return new Reservation(member, date, time, theme);
    }
}
