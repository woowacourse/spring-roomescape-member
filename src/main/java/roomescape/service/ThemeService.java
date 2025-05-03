package roomescape.service;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ForeignKeyConstraintViolationException;
import roomescape.exception.ResourceNotExistException;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> getAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse save(ThemeRequest request) {
        validateThemeName(request);
        Theme theme = new Theme(
                request.name(),
                request.description(),
                request.thumbnail()
        );
        return getThemeResponse(theme);
    }

    public void deleteById(Long id) {
        validateIsInUse(id);
        int count = themeDao.deleteById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }

    public List<ThemeResponse> getPopularThemes(int count) {
        List<Theme> themes = themeDao.findPopular(count);
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private void validateThemeName(ThemeRequest request) {
        if (themeDao.getCountByName(request.name()) != 0) {
            throw new IllegalArgumentException("[ERROR] 해당 테마 이름이 이미 존재합니다.");
        }
    }

    private void validateIsInUse(Long id) {
        if (reservationDao.getCountByThemeId(id) != 0) {
            throw new ForeignKeyConstraintViolationException();
        }
    }

    private ThemeResponse getThemeResponse(Theme theme) {
        try {
            return ThemeResponse.from(themeDao.save(theme));
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 해당 테마 이름이 이미 존재합니다.");
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 테마 생성에 실패했습니다.");
        }
    }
}
