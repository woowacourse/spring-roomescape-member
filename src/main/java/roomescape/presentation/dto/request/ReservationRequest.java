package roomescape.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(

        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull
        LocalDate date,

        @NotBlank
        String name,

        @NotNull
        Long timeId
) {
}
