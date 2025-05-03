package roomescape.service;

import java.util.List;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

public interface ThemeService {

    List<ThemeResponseDto> getAllThemes();

    ThemeResponseDto saveTheme(ThemeRequestDto request);

    void deleteTheme(Long id);

    List<ThemeResponseDto> getAllThemeOfRanks();
}
