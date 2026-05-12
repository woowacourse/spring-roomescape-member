package roomescape.reservation.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotNull(message = "테마 정보가 필요합니다.")
        Long themeId,
        @NotBlank(message = "이름은 비어있을 수 없습니다.")
        String name,
        @NotNull(message = "날짜를 입력해주세요.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull(message = "예약 시간 정보가 필요합니다.")
        Long timeId) {
}
