package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

@Service
public class ThemeService {
    public static final int RANKING_LIMIT = 10;
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponseDto create(ThemeRequestDto requestDto) {
        Theme theme = requestDto.toEntity();
        return ThemeResponseDto.from(themeDao.create(theme));
    }

    public List<ThemeResponseDto> findAll() {
        return themeDao.findAll().stream()
                .map(ThemeResponseDto::from)
                .toList();
    }

    public void delete(Long id) {
        themeDao.delete(id);
    }

    public List<ThemeResponseDto> findRanking(LocalDate startDate, LocalDate endDate) {
        return themeDao.findRanking(startDate, endDate, RANKING_LIMIT).stream()
                .map(ThemeResponseDto::from)
                .toList();
    }
}
