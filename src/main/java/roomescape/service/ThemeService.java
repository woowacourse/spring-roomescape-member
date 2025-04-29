package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.model.Theme;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> getAllThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponseDto::from)
                .collect(Collectors.toList());
    }

    public ThemeResponseDto saveTheme(ThemeRequestDto themeRequestDto) {
        Theme theme = themeRequestDto.convertToTheme();
        Long savedId = themeDao.saveTheme(theme);
        return new ThemeResponseDto(savedId, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
