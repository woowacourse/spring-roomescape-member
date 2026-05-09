package roomescape.dto.theme;

import java.util.List;

public record ThemesResponseDto(
        List<ThemeResponseDto> themes
) {
}
