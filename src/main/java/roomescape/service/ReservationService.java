package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public Reservation save(String name, LocalDate date, long timeId, long themeId) {
        final ReservationTime time = reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
        final Theme theme = themeDao.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
        new Reservation(null, name, date, LocalDate.now(), time, theme);
        final long id = reservationDao.save(name, date, timeId, themeId);
        return new Reservation(id, name, date, null, time, theme);
    }

    public void delete(long id) {
        reservationDao.delete(id);
    }

    public List<Reservation> findAllByName(String username) {
        return reservationDao.findByName(username);
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }
}
