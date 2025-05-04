package roomescape.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record CreateReservationRequest(
        @NotBlank(message = "이름은 필수입니다")
        String name,

        @NotNull(message = "예약 날짜는 필수입니다")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Positive(message = "유효하지 않은 timeId입니다")
        long timeId,

        @Positive(message = "유효하지 않은 themeId입니다")
        long themeId) {
}
