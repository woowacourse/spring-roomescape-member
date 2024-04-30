package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeResponseDto;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> findAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(ThemeResponseDto::new)
                .toList();
    }
}
