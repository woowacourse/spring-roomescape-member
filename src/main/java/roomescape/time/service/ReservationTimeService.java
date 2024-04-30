package roomescape.time.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime save(ReservationTime reservationTime) {
        return reservationTimeDao.save(reservationTime);
    }

    public void delete(long id) {
        reservationTimeDao.delete(id);
    }
}
