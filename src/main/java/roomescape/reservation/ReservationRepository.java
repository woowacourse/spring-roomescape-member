package roomescape.reservation;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ReservationRepository {
    private final ReservationDao reservationDao;

    public ReservationRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation save(Reservation reservation) {
        return reservationDao.save(reservation);
    }

    public void delete(long id) {
        reservationDao.delete(id);
    }

    public int countByTimeId(long timeId) {
        return reservationDao.countByTimeId(timeId);
    }

    public Optional<Reservation> findById(long id) {
        return reservationDao.findById(id);
    }
}
