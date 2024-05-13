package roomescape.theme.theme.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomEscapeException;
import roomescape.theme.theme.dao.ThemeDao;
import roomescape.theme.theme.domain.Theme;
import roomescape.theme.theme.dto.ThemeResponse;
import roomescape.theme.theme.dto.ThemeSaveRequest;
import roomescape.theme.theme.mapper.ThemeMapper;
import roomescape.theme.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static roomescape.global.exception.ExceptionMessage.THEME_EXIST_RESERVATION_CANNOT_DELETE;

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
            throw new RoomEscapeException(BAD_REQUEST, THEME_EXIST_RESERVATION_CANNOT_DELETE.getMessage());
        }
    }
}
