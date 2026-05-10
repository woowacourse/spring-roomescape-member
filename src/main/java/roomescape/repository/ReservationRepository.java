package roomescape.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.global.exception.ReservationNotFoundException;
import roomescape.domain.Reservation;
import roomescape.repository.dao.ReservationDao;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final ReservationDao reservationDao;

    public List<Reservation> findAll() {
        return reservationDao.selectAll();
    }

    public Reservation save(Reservation reservation) {
        Long id = reservationDao.insert(reservation);
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    public void deleteById(Long id) {
        int deletedCount = reservationDao.deleteById(id);

        if (deletedCount == 0) {
            throw new ReservationNotFoundException();
        }
    }
}
