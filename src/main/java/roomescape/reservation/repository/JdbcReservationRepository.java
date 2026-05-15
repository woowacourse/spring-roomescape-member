package roomescape.reservation.repository;

import java.time.LocalDate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dao.ReservationDao;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final ReservationDao reservationDao;

    public JdbcReservationRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationDao.save(reservation);
    }

    @Override
    public void delete(long id) {
        reservationDao.delete(id);
    }

    @Override
    public int countByTimeId(long timeId) {
        return reservationDao.countByTimeId(timeId);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        return reservationDao.findById(id);
    }

    @Override
    public boolean existsByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        return reservationDao.existsByDateTimeAndTheme(date, timeId, themeId);
    }
}
