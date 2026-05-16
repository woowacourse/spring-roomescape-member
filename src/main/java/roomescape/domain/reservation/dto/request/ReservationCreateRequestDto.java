package roomescape.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationCreateRequestDto(@NotBlank String name,
                                          @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                          @NotNull @Min(1) Long timeId,
                                          @NotNull @Min(1) Long themeId) {

}
