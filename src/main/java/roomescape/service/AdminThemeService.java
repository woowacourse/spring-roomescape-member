package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;

@Service
public class AdminThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public AdminThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public long save(String name, String description, String thumbnailUrl) {
        return themeDao.save(name, description, thumbnailUrl);
    }

    public void delete(long id) {
        if (reservationDao.existsByThemeId(id)) {
            throw new IllegalArgumentException("예약에 사용 중인 테마는 삭제할 수 없습니다.");
        }
        themeDao.delete(id);
    }
}