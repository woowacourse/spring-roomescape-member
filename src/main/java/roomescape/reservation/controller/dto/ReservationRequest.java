package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") LocalDate date,
        @NotBlank Long timeId,
        @NotBlank Long themeId,
        @NotBlank Long memberId
) {

    public ReservationRequest {
        validateDate(date);
        validateTimeId(timeId);
        validateThemeId(themeId);
        validateMemberId(memberId);
    }

    public Reservation toReservationWithoutId(Member member, ReservationTime time, Theme theme) {
        return new Reservation(null, member, date, time, theme);
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
    }

    private void validateTimeId(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("예약 시간 ID는 필수입니다.");
        }
    }

    private void validateThemeId(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마 ID는 필수입니다.");
        }
    }

    private void validateMemberId(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("멤버 ID는 필수입니다.");
        }
    }

}

