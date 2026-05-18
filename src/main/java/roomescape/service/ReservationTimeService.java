package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return ReservationTimeResponse.from(reservationTimeDao.findAllReservationTimes());
    }

    @Transactional
    public ReservationTimeResponse createReservationTime(ReservationTimeCreateRequest request) {
        ReservationTime time = ReservationTime.from(request.startAt());
        Long id = reservationTimeDao.insertReservationTime(time);
        return ReservationTimeResponse.from(reservationTimeDao.findById(id));
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        int deleteCount = reservationTimeDao.delete(id);
        ReservationTime.validateDeletion(deleteCount);
    }
}
