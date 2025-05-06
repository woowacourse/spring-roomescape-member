package roomescape.fake;

import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.exception.ResourceNotExistException;

import java.time.LocalTime;
import java.util.List;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final FakeReservationTimeDao reservationTimeDao;

    public FakeReservationTimeRepository() {
        this.reservationTimeDao = new FakeReservationTimeDao();
    }

    @Override
    public ReservationTime findById(final Long id) {
        try {
            return reservationTimeDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotExistException();
        }
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        return reservationTimeDao.save(reservationTime);
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    @Override
    public int deleteById(final Long id) {
        return reservationTimeDao.deleteById(id);
    }

    @Override
    public boolean existByTimeValue(final LocalTime time) {
        return reservationTimeDao.existByTimeValue(time);
    }

    public void clear() {
        reservationTimeDao.clear();
    }
}
