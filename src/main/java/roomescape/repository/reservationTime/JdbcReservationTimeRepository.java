package roomescape.repository.reservationTime;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTime.ReservationTimeCommand;

@Repository
@Profile("web")
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private final ReservationTimeDao reservationTimeDao;

    public JdbcReservationTimeRepository(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    @Override
    public ReservationTime addReservationTime(ReservationTimeCommand reservationTimeCommand) {
        return new ReservationTime(reservationTimeDao.insertReservationTime(reservationTimeCommand), reservationTimeCommand.startAt());
    }

    @Override
    public Optional<ReservationTime> getReservationTime(long id) {
        return reservationTimeDao.getReservationTime(id);
    }

    @Override
    public List<ReservationTime> getAllReservationTime() {
        return Collections.unmodifiableList(reservationTimeDao.getAllReservationTime());
    }

    @Override
    public void deleteReservationTime(long id) {
        reservationTimeDao.deleteReservationTime(id);
    }
}
