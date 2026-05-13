package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservationCreateRequest(
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotBlank
        @Size(min = 2, max = 10)
        @Pattern(regexp = "^[a-zA-Z가-힣]+$")
        String name,

        @NotNull
        @Positive
        Long timeId,

        @NotNull
        @Positive
        Long themeId
) {
}
