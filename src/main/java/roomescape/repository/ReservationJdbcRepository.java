package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {
    private final ReservationDao reservationDao;

    public ReservationJdbcRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public Reservation save(Reservation reservation) {
        Long id = reservationDao.insert(reservation);
        return new Reservation(id, reservation);
    }

    @Override
    public List<Reservation> findAllByDateAndTimeAndThemeId(LocalDate date, ReservationTime time, Long themeId) {
        return reservationDao.selectAllByDateAndTime(date, time, themeId);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationDao.selectAll();
    }

    @Override
    public boolean existById(Long id) {
        return reservationDao.existById(id);
    }

    @Override
    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    @Override
    public int countByTimeId(Long timeId) {
        return reservationDao.countByTimeId(timeId);
    }

    @Override
    public List<Long> findAllTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        return reservationDao.selectAllTimeIdsByDateAndThemeId(date, themeId);
    }
}
