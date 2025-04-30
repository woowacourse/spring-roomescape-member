package roomescape.dto;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequest(
    // TODO : valid 사항 표시해주기
    @NotBlank String name,
    String description,
    String thumbnail
) {
}
