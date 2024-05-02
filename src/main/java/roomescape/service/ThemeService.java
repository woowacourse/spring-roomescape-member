package roomescape.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.IllegalThemeException;
import roomescape.mapper.ThemeMapper;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {

    private final ThemeMapper themeMapper = new ThemeMapper();
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> findAllThemes() {
        List<Theme> themes = themeDao.findAll();

        return themes.stream()
                .map(themeMapper::mapToResponse)
                .toList();
    }

    public List<ThemeResponse> findRankThemes() {
        List<Theme> themesByDescOrder = themeDao.findThemesByDescOrder();

        return themesByDescOrder.stream()
                .map(themeMapper::mapToResponse)
                .toList();
    }

    public ThemeResponse save(ThemeRequest request) {
        Theme theme = themeMapper.mapToTheme(request);

        Theme newTheme = themeDao.save(theme);
        return themeMapper.mapToResponse(newTheme);
    }

    public void deleteThemeById(Long id) {
        try {
            themeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalThemeException("[ERROR] 예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
    }
}
