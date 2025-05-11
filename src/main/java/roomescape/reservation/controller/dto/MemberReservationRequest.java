package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record MemberReservationRequest(
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") LocalDate date,
        Long timeId,
        Long themeId
) {

    public MemberReservationRequest {
        validateDate(date);
        validateTimeId(timeId);
        validateThemeId(themeId);
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

}

