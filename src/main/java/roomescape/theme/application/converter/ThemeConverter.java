package roomescape.theme.application.converter;

import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeThumbnail;
import roomescape.theme.infrastructure.entity.ThemeEntity;
import roomescape.theme.ui.dto.ThemeResponse;

import java.util.List;

public class ThemeConverter {

    public static Theme toDomain(final ThemeEntity entity) {
        return Theme.withId(
                ThemeId.from(entity.getId()),
                ThemeName.from(entity.getName()),
                ThemeDescription.from(entity.getDescription()),
                ThemeThumbnail.from(entity.getThumbnail())
        );
    }

    public static ThemeResponse toDto(final Theme theme) {
        return new ThemeResponse(
                theme.getId().getValue(),
                theme.getName().getValue(),
                theme.getDescription().getValue(),
                theme.getThumbnail().getValue());
    }

    public static List<ThemeResponse> toDto(final List<Theme> themes) {
        return themes.stream()
                .map(ThemeConverter::toDto)
                .toList();
    }
}
