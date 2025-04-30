package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.dao.ThemeDao;
import roomescape.domain_entity.Theme;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

@Component
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponseDto createTheme(ThemeRequestDto themeRequestDto) {
        Theme themeWithoutId = themeRequestDto.toTheme();
        Long id = themeDao.create(themeWithoutId);
        Theme created = themeWithoutId.copyWithId(id);
        return new ThemeResponseDto(created);
    }
}
