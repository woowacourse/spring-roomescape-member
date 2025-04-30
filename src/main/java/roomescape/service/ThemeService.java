package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
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
}
