package roomescape.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DateTimeFormat;

public record ReservationRequest(
        @NotBlank(message = "이름을 입력해주세요.")
        String name,
        @NotNull(message = "날짜를 입력해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd", message = "날짜 형식이 올바르지 않습니다.")
        String date,
        @NotNull(message = "시간 ID를 입력해주세요.")
        Long timeId,
        @NotNull(message = "테마 ID를 입력해주세요.")
        Long themeId) {

    public Reservation toReservation(ReservationTime reservationTime, Theme theme) {
        return new Reservation(new PlayerName(name), parsedDate(), reservationTime, theme);
    }

    public LocalDate parsedDate() {
        return LocalDate.parse(date);
    }
}
