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

    public ReservationTimeResponse create(ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.startAt());
        reservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.from(reservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll()
            .stream()
            .map(ReservationTimeResponse::from)
            .toList();
    }

    public boolean deleteReservationTime(Long id) {
        int deleteCount = reservationTimeDao.deleteById(id);
        return deleteCount != 0;
    }
}
