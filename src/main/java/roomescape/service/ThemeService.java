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

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> getAllThemes() {
        return themeDao.findAllTheme().stream().map(ThemeResponseDto::from).toList();
    }

    public ThemeResponseDto saveTheme(ThemeRequestDto request) {
        Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        themeDao.saveTheme(theme);
        return ThemeResponseDto.from(theme);
    }

    public void deleteTheme(Long id) {
        validateIsExistThemeBy(id);
        themeDao.deleteTheme(id);
    }

    private void validateIsExistThemeBy(Long id) {
        themeDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마를 찾을 수 없습니다: " + id));
    }

    public List<ThemeResponseDto> getAllThemeOfRanks() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(7);
        List<Theme> themes = themeDao.findAllThemeOfRanks(startDate, currentDate);
        return themes.stream().map(ThemeResponseDto::from).toList();
    }
}
