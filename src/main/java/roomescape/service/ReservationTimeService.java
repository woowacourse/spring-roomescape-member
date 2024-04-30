package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll()
                .stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        long id = reservationTimeDao.save(reservationTimeRequest.toReservationTime()); //TODO 도메인 반환
        return new ReservationTimeResponse(id, reservationTimeRequest.startAt());
    }

    public boolean deleteById(long id) {
        return reservationTimeDao.deleteById(id);
    }
}
