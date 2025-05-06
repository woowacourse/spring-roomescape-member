package roomescape.fake;

import roomescape.domain.model.ReservationTime;
import roomescape.domain.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final FakeReservationTimeDao reservationTimeDao;

    public FakeReservationTimeRepository() {
        this.reservationTimeDao = new FakeReservationTimeDao();
    }

    @Override
    public ReservationTime findById(final Long id) {
        return reservationTimeDao.findById(id);
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
