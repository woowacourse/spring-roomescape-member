package roomescape.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import roomescape.application.dto.request.ReservationCreationRequest;

public record ReservationRequest(
        @NotNull(message = "예약날짜는 필수입니다.")
        LocalDate date,

        @NotNull(message = "시간 아이디는 필수입니다.")
        @Positive(message = "시간 아이디는 양수여야 합니다.")
        Long timeId,

        @NotNull(message = "테마 아이디는 필수입니다.")
        @Positive(message = "테마 아이디는 양수여야 합니다.")
        Long themeId
) {
    public ReservationCreationRequest toReservationCreationRequest(long memberId) {
        return new ReservationCreationRequest(date, timeId, themeId, memberId);
    }
}
