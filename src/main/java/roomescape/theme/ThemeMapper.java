package roomescape.theme;

import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeResponseDto;

public class ThemeMapper {

    public static ThemeResponseDto toResponseDto(Theme theme) {
        return new ThemeResponseDto(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
