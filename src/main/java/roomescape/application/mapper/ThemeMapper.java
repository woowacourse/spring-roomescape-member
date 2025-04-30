package roomescape.application.mapper;

import jakarta.validation.Valid;
import java.util.List;
import roomescape.domain.Theme;
import roomescape.presentation.dto.request.ThemeRequest;
import roomescape.presentation.dto.response.ThemeResponse;

public class ThemeMapper {
    public static ThemeResponse toDto(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public static List<ThemeResponse> toDtos(List<Theme> themes) {
        return themes.stream()
                .map(ThemeMapper::toDto)
                .toList();
    }

    public static Theme toDomain(@Valid ThemeRequest themeRequest) {
        return Theme.withoutId(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
    }
}
