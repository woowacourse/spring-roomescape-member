package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotNull(message = "날짜를 입력해주세요.") LocalDate date,
        @NotNull(message = "시간을 입력해주세요.") Long timeId,
        @NotNull(message = "테마를 입력해주세요.") Long themeId
) {

    public CreateReservationInfo convertToCreateReservationInfo(final long memberId) {
        return new CreateReservationInfo(date, memberId, timeId, themeId);
    }
}
