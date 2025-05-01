package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public record ReservationRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationRequest {
        validateName(name);
        validateDate(date);
        validateTimeId(timeId);
        validateThemeId(themeId);
    }

    public Reservation toReservationWithoutId(ReservationTime time, Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
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
        if(themeId == null){
            throw new IllegalArgumentException("테마 ID는 필수입니다.");
        }
    }

}

