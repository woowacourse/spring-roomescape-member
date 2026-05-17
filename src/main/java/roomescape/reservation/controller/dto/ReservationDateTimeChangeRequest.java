package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ReservationDateTimeChangeRequest(
        @NotNull(message = "예약 날짜는 필수입니다.")
        LocalDate date,
        @Positive(message = "예약 시간 ID는 양수여야 합니다.")
        long timeId
) {
}
