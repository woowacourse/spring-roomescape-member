package roomescape.theme.application.converter;

import roomescape.theme.application.dto.CreateThemeServiceRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeThumbnail;
import roomescape.theme.infrastructure.entity.ThemeDBEntity;
import roomescape.theme.ui.dto.CreateThemeWebRequest;
import roomescape.theme.ui.dto.ThemeResponse;

import java.util.List;

public class ThemeConverter {

    public static Theme toDomain(final ThemeDBEntity entity) {
        return Theme.withId(
                ThemeId.from(entity.getId()),
                ThemeName.from(entity.getName()),
                ThemeDescription.from(entity.getDescription()),
                ThemeThumbnail.from(entity.getThumbnail())
        );
    }

    public static Theme toDomain(final CreateThemeServiceRequest createThemeServiceRequest) {
        return Theme.withoutId(
                ThemeName.from(createThemeServiceRequest.name()),
                ThemeDescription.from(createThemeServiceRequest.description()),
                ThemeThumbnail.from(createThemeServiceRequest.thumbnail())
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

    public static CreateThemeServiceRequest toServiceDto(final CreateThemeWebRequest createThemeWebRequest) {
        return new CreateThemeServiceRequest(
                createThemeWebRequest.name(),
                createThemeWebRequest.description(),
                createThemeWebRequest.thumbnail()
        );
    }
}
