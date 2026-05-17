package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "이름은 빈값일 수 없습니다.")
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @FutureOrPresent
        LocalDate date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId) {
}
