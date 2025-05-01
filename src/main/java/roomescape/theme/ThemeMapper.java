package roomescape.theme;

import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeResDto;

public class ThemeMapper {

    public static ThemeResDto toResDto(Theme theme) {
        return new ThemeResDto(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
