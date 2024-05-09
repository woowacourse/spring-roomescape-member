package roomescape.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

import java.time.LocalDate;

public record AdminReservationRequest(
        @FutureOrPresent(message = "지난 날짜는 예약할 수 없어요") LocalDate date,
        @Positive @NotNull(message = "원하는 시간을 지정해 주세요") long timeId,
        @Positive @NotNull(message = "원하는 테마를 지정해 주세요") long themeId,
        @Positive @NotNull(message = "멤버를 지정해 주세요") long memberId
) {
    public Reservation toEntity(final ReservationTime reservationTime, final Theme theme, final Member member) {
        return new Reservation(null, date, reservationTime, theme, member);
    }
}
