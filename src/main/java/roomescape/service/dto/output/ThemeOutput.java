package roomescape.service.dto.output;

import java.util.List;
import roomescape.domain.Theme;

public record ThemeOutput(long id, String name, String description, String thumbnail) {

    public static ThemeOutput from(final Theme theme) {
        return new ThemeOutput(theme.id(), theme.name(), theme.description(), theme.getThumbnailAsString());
    }

    public static List<ThemeOutput> list(final List<Theme> themes) {
        return themes.stream()
                .map(ThemeOutput::from)
                .toList();
    }
}
