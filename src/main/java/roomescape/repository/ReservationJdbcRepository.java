package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public List<Reservation> findAllByDateAndTime(LocalDate date, ReservationTime time) {
        return reservationDao.selectAllByDateAndTime(date, time);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationDao.selectAll();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservationDao.selectById(id));
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
    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        return reservationDao.selectAllByDateAndThemeId(date, themeId);
    }
}
