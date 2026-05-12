package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> times = reservationTimeDao.findAll();

        return times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeRequest request) {
        ReservationTime time = new ReservationTime(
                request.startAt()
        );

        ReservationTime saved = reservationTimeDao.save(time);

        return ReservationTimeResponse.from(saved);
    }

    public void delete(Long id) {
        reservationTimeDao.delete(id);
    }
}
