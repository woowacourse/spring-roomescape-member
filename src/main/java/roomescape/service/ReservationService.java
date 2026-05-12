package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

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

    public List<Reservation> findAll(int page, int size) {
        return reservationDao.findAll(page, size);
    }

    public Reservation save(String name, LocalDate date, Long timeId, Long themeId) {
        if (reservationDao.existByDateAndTimeAndThemeId(date, timeId, themeId)) {
            throw new IllegalArgumentException("이미 존재하는 예약입니다.");
        }
        ReservationTime time = reservationTimeDao.findById(timeId);

        Theme theme = themeDao.findById(themeId);
        Reservation reservation = Reservation.create(
                name,
                date,
                time,
                theme,
                LocalDateTime.now()
        );
        return reservationDao.save(reservation);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

}
