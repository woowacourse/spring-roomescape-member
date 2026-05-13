package roomescape.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record ErrorResponse(
        @NotBlank
        String code,

        @NotBlank
        String path,

        @NotBlank
        String message,

        @Nullable
        String action) {
}
