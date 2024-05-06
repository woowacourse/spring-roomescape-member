package roomescape.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.ExistReservationException;
import roomescape.exception.IllegalThemeException;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> findAllThemes() {
        List<Theme> themes = themeDao.findAll();

        return ThemeResponse.fromThemes(themes);
    }

    public List<ThemeResponse> findRankThemes() {
        List<Theme> themesByDescOrder = themeDao.findThemesByDescOrder();

        return ThemeResponse.fromThemes(themesByDescOrder);
    }

    public long save(ThemeCreateRequest request) {
        Theme theme = ThemeCreateRequest.toTheme(request);

        if (themeDao.existByName(theme.getName())) {
            throw new IllegalThemeException("[ERROR] 중복된 테마는 생성할 수 없습니다.");
        }

        return themeDao.save(theme);
    }

    public void deleteThemeById(Long id) {
        try {
            themeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ExistReservationException("[ERROR] 예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
    }
}
