package roomescape.domain.theme.dto.response;

import roomescape.domain.theme.entity.Theme;

public record ThemeResponseDto(Long id, String name, String description, String imageUrl) {

    public static ThemeResponseDto from(Theme theme) {
        return new ThemeResponseDto(theme.getId(), theme.getName(), theme.getDescription(),
            theme.getImageUrl());
    }
}
