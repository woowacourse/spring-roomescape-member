package roomescape.dto;

import jakarta.validation.constraints.NotBlank;

public record ErrorResponse(
        @NotBlank
        String code,

        @NotBlank
        String path,

        @NotBlank
        String message) {
}
