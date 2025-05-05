package roomescape.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ThemeRequest(

        @NotBlank
        @NotNull
        String name,

        @NotBlank
        @NotNull
        String description,

        @NotBlank
        @NotNull
        String thumbnail
) {
}
