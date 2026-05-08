package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ThemeRequest;
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

    public Long save(ThemeRequest request) {
        return themeDao.save(request.name(), request.description(), request.thumbnailUrl());
    }

    public void delete(long id) {
        if (reservationDao.existsByThemeId((long) id)) {
            throw new IllegalArgumentException("예약에 사용 중인 테마는 삭제할 수 없습니다.");
        }
        themeDao.delete(id);
    }
}
