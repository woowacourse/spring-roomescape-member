package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationRequest(
        @NotNull(message = "예약 날짜를 입력해 주세요.")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,
        @NotNull(message = "시간을 선택해 주세요.")
        Long timeId,
        @NotNull(message = "테마를 선택해 주세요.")
        Long themeId,
        Long memberId
) {
    public ReservationRequest withMemberId(Long memberId) {
        return new ReservationRequest(date, timeId, themeId, memberId);
    }

    public Reservation toReservationWithoutId(ReservationTime time, Theme theme, Member member) {
        return new Reservation(null, date, time, theme, member);
    }

}

