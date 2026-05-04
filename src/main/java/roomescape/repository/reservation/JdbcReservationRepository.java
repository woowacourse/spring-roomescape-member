package roomescape.repository.reservation;

import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;

@Repository
@Profile("web")
public class JdbcReservationRepository implements ReservationRepository {
    private final ReservationDao reservationDao;

    public JdbcReservationRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public List<Reservation> getAllReservation() {
        return Collections.unmodifiableList(reservationDao.getAllReservation());
    }

    @Override
    public Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime) {
        long id = reservationDao.insertReservation(reservationCommand);
        return new Reservation(id, reservationCommand.name(), reservationCommand.date(), reservationTime);
    }

    @Override
    public void deleteReservation(long id) {
        reservationDao.deleteReservation(id);
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        return reservationDao.existsByTimeId(timeId);
    }
}
