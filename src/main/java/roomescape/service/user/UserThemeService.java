package roomescape.service.user;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ReservationDao;

@Service
public class UserThemeService {

    private final ReservationDao reservationDao;

    public UserThemeService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Theme> getThemeRanking() {
        return reservationDao.findThemeOrderByReservationCount();
    }
}
