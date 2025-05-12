package roomescape.reservation.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationReqDto(
    @NotNull(message = "예약 날짜는 필수입니다.")
    LocalDate date,
    @NotNull(message = "예약 시간은 필수입니다.")
    Long timeId,
    @NotNull(message = "테마 ID는 필수입니다.")
    Long themeId) {
}
