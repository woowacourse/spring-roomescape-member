package roomescape.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

// TODO 날짜 제약 걸기
public record ReservationRequest(
        @NotBlank @Size(max = 25) String name,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull Long timeId
) {
}
