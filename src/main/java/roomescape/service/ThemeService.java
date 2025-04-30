package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> getAllThemes() {
        return themeDao.findAllTheme().stream()
            .map(ThemeResponseDto::from)
            .toList();
    }

    public ThemeResponseDto saveTheme(ThemeRequestDto request) {
        Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        themeDao.saveTheme(theme);
        return ThemeResponseDto.from(theme);
    }
}
