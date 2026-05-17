package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "예약자 이름은 빈값일 수 없습니다.")
        @Size(max = 20, message = "예약자 이름은 20자 이하여야 합니다.")
        String name,

        @NotNull(message = "예약 날짜는 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "예약 시간은 필수입니다.")
        Long timeId,

        @NotNull(message = "예약 테마는 필수입니다.")
        Long themeId) {
}
