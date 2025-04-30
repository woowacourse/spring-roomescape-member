package roomescape.service;

import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ResourceNotExistException;
import roomescape.exception.ThemeConstraintException;

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
        if (themeDao.getCountByName(request.name()) != 0) {
            throw new IllegalArgumentException("[ERROR] 해당 테마 이름이 이미 존재합니다.");
        }

        Theme theme = new Theme(
            request.name(),
            request.description(),
            request.thumbnail()
        );

        try {
            return ThemeResponse.from(themeDao.save(theme));
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 해당 테마 이름이 이미 존재합니다.");
        }
    }

    public void deleteById(Long id) {
        if (reservationDao.getCountByThemeId(id) != 0) {
            // TODO: dao에서 던지는 예외와 service에서 던지는 예외가 과연 같아도 되는가
            throw new ThemeConstraintException();
        }

        int count = themeDao.deleteById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }

    public List<ThemeResponse> getTop10() {
        List<Theme> themes = themeDao.findTop10();
        return themes.stream()
            .map(ThemeResponse::from)
            .toList();
    }
}
