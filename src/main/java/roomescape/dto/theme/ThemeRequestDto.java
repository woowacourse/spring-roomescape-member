package roomescape.dto.theme;

import roomescape.domain.Theme;

public record ThemeRequestDto(
    String name,
    String description,
    String imageUrl
) { }
