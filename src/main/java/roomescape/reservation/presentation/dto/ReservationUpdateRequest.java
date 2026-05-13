package roomescape.reservation.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        @NotNull(message = "날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull(message = "시간 ID는 필수입니다.")
        Long timeId
) {
}
