package roomescape.domain.theme.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ThemeCreateRequestDto(@NotBlank String name, @NotBlank String description,
                                    @NotBlank String imageUrl) {

}
