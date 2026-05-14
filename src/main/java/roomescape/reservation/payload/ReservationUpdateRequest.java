package roomescape.reservation.payload;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequest(
        @NotNull(message = "예약 날짜는 필수입니다.")
        LocalDate date,
        @NotNull(message = "예약 시간id는 필수입니다.")
        Long timeId
) {
}
