package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.JdbcThemeDao;
import roomescape.dto.ThemeResponseDto;

@Service
public class ThemeService {
    private final JdbcThemeDao jdbcThemeDao;

    public ThemeService(JdbcThemeDao jdbcThemeDao) {
        this.jdbcThemeDao = jdbcThemeDao;
    }

    public List<ThemeResponseDto> readAll() {
        return jdbcThemeDao.readAll().stream()
                .map(ThemeResponseDto::from)
                .toList();
    }
}
