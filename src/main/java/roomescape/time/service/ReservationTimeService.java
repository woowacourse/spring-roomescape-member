package roomescape.time.service;

import java.time.LocalTime;
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
        if (exists(reservationTime.startAt())) {
            throw new IllegalArgumentException("StartAt already exists");
        }
        return reservationTimeDao.save(reservationTime);
    }

    public boolean exists(LocalTime startAt) {
        return reservationTimeDao.exists(startAt);
    }

    public void delete(long id) {
        reservationTimeDao.delete(id);
    }
}
