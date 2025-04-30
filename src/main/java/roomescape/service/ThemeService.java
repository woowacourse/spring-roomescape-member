package roomescape.service;

import java.util.List;
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
        Theme theme = new Theme(
            request.name(),
            request.description(),
            request.thumbnail()
        );
        return ThemeResponse.from(themeDao.save(theme));
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
}
