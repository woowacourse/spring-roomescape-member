package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.JdbcThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

@Service
public class ThemeService {
    private final JdbcThemeDao jdbcThemeDao;

    public ThemeService(JdbcThemeDao jdbcThemeDao) {
        this.jdbcThemeDao = jdbcThemeDao;
    }

    public ThemeResponseDto create(ThemeRequestDto requestDto) {
        Theme theme = requestDto.toEntity();
        return ThemeResponseDto.from(jdbcThemeDao.create(theme));
    }

    public List<ThemeResponseDto> readAll() {
        return jdbcThemeDao.readAll().stream()
                .map(ThemeResponseDto::from)
                .toList();
    }

    public void delete(Long id) {
        jdbcThemeDao.delete(id);
    }
}
