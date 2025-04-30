package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Theme;

@Service
public class ThemeService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    public ThemeService(ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public ThemeResponse addTheme(ThemeRequest themeRequest) {
        Theme saved = themeDao.save(themeRequest.toEntity());
        return ThemeResponse.from(saved);
    }

    public List<ThemeResponse> getThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteTheme(Long id) {
        if (reservationDao.isExistByThemeId(id)) {
            throw new IllegalStateException("예약이 존재하여 삭제할 수 없습니다.");
        }
        boolean isDeleted = themeDao.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("해당하는 ID가 없습니다.");
        }
    }
}
