package roomescape.reservation.domain.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ReservationReqDto(
    @NotBlank(message = "예약자 이름은 필수입니다.")
    String name,
    @NotBlank(message = "예약 날짜는 필수입니다.")
    LocalDate date,
    @NotBlank(message = "예약 시간은 필수입니다.")
    Long timeId,
    @NotBlank(message = "테마 ID는 필수입니다.")
    Long themeId) {
}
