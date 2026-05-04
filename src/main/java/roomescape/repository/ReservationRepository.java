package roomescape.repository;

import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;

import java.util.List;

@Component
public class ReservationRepository {

    private final ReservationDao reservationDao;

    public ReservationRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Long save(Reservation reservation) {
        return reservationDao.save(reservation);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public boolean existsByTimeId(Long timeId) {
        return reservationDao.existsByTimeId(timeId);
    }
}
