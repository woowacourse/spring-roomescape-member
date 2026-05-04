package roomescape.dto.theme;

import java.util.List;
import roomescape.domain.Theme.Theme;

public record AllThemeResponse(List<Theme> themes) {
}
