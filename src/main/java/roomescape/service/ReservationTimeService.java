package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.CreateReservationTimeRequest;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime createReservationTime(CreateReservationTimeRequest request) {
        Long newReservationTimeId = reservationTimeDao.save(request);
        return reservationTimeDao.findById(newReservationTimeId);
    }

    public void deleteReservationTime(Long id) {
        reservationTimeDao.deleteById(id);
    }
}
