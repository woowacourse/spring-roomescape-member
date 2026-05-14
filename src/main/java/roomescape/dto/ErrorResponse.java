package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String action) {
}
