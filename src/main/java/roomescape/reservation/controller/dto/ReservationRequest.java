package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.global.exception.error.InvalidRequestException;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationRequest(
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") LocalDate date,
        Long timeId,
        Long themeId,
        Long memberId
) {

    public ReservationRequest {
        validateDate(date);
        validateTimeId(timeId);
        validateThemeId(themeId);
    }

    public ReservationRequest withMemberId(Long memberId) {
        return new ReservationRequest(date, timeId, themeId, memberId);
    }

    public Reservation toReservationWithoutId(ReservationTime time, Theme theme, Member member) {
        return new Reservation(null, date, time, theme, member);
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidRequestException("예약 날짜는 필수입니다.");
        }
    }

    private void validateTimeId(Long timeId) {
        if (timeId == null) {
            throw new InvalidRequestException("예약 시간 ID는 필수입니다.");
        }
    }

    private void validateThemeId(Long themeId) {
        if (themeId == null) {
            throw new InvalidRequestException("테마 ID는 필수입니다.");
        }
    }

}

