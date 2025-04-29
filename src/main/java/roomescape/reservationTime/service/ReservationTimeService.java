package roomescape.reservationTime.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.reservationTime.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final Dao<ReservationTime> reservationTimeDao;

    public ReservationTimeService(Dao<ReservationTime> reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse add(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime newReservationTime = new ReservationTime(null, reservationTimeRequest.startAt());
        ReservationTime savedReservationTime = reservationTimeDao.add(newReservationTime);
        return new ReservationTimeResponse(savedReservationTime.id(), savedReservationTime.startAt());
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(reservationTime -> new ReservationTimeResponse(
                        reservationTime.id(), reservationTime.startAt()))
                .toList();
    }

    public void deleteById(Long id) {
        reservationTimeDao.deleteById(id);
    }
}
