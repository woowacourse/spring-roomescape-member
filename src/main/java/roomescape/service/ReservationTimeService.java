package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.ReservationTimeNotFoundException;

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
        Long id = reservationTimeDao.insertReservationTime(request.startAt());
        return ReservationTimeResponse.from(reservationTimeDao.findById(id));
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        int deleteCount = reservationTimeDao.delete(id);
        validateDelete(deleteCount);
    }

    private void validateDelete(int deleteCount) {
        if (deleteCount == 0) {
            throw new ReservationTimeNotFoundException();
        }
    }
}
