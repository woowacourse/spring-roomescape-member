package roomescape.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationCreateDto(

        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull
        LocalDate date,

        @NotNull
        Long themeId,

        @NotNull
        Long timeId,

        @NotNull
        Long memberId
) {
}
