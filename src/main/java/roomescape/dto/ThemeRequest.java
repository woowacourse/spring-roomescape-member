package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ThemeRequest (
        @Size(max=255)
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        String thumbnailUrl){
}
