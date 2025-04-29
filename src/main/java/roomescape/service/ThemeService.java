package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
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
        return themeDao.findAll().stream()
                .map(ThemeResponseDto::from)
                .collect(Collectors.toList());
    }
}
