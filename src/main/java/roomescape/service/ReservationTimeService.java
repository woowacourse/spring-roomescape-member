package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTime save(ReservationTime reservationTime) {
        return reservationTimeDao.save(reservationTime);
    }

    public void delete(Long id) {
        reservationTimeDao.delete(id);
    }
}
