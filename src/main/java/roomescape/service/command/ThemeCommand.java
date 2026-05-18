package roomescape.service.command;

import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

public record ThemeCommand(
        ThemeName name,
        String description,
        ThemeImageUrl imageUrl
) {
}
