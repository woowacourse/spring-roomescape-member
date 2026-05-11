package roomescape.domain.theme.mapper;

import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.theme.entity.Theme;

public final class ThemeMapper {

    private ThemeMapper() {

    }

    public static ThemeResponseDTO toResponseDTO(Theme theme) {
        return new ThemeResponseDTO(theme.getId(), theme.getName(), theme.getDescription(), theme.getImageUrl());
    }
}
