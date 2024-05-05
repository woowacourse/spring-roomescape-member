package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;
import roomescape.exception.IllegalThemeException;
import roomescape.mapper.ThemeMapper;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private final ThemeMapper themeMapper = new ThemeMapper();
    private final ThemeDao themeDao;
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeDao themeDao, ThemeRepository themeRepository) {
        this.themeDao = themeDao;
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findAllThemes() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(themeMapper::mapToResponse)
                .toList();
    }

    public List<ThemeResponse> findBestThemes() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysBefore = today.minusDays(7);
        LocalDate oneDayBefore = today.minusDays(1);

        List<Theme> themesByDescOrder = themeRepository.findBest10Between(sevenDaysBefore, oneDayBefore);
        return themesByDescOrder.stream()
                .map(themeMapper::mapToResponse)
                .toList();
    }

    public ThemeResponse save(ThemeSaveRequest request) {
        Theme theme = themeMapper.mapToTheme(request);

        Long saveId = themeDao.save(theme);
        return themeMapper.mapToResponse(saveId, theme);
    }

    public void deleteThemeById(Long id) {
        try {
            themeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalThemeException("[ERROR] 예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
    }
}
