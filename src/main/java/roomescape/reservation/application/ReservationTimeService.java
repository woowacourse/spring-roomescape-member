package roomescape.reservation.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.infrastructure.ReservationTimeDao;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ReservationTimeResponse;
import roomescape.reservation.domain.ReservationTime;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse createReservationTime(final ReservationTimeRequest reservationTimeRequest) {
        return new ReservationTimeResponse(reservationTimeDao.insert(reservationTimeRequest.getStartAt()));
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeDao.findAllTimes().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteReservationTime(final Long id) {
        reservationTimeDao.delete(id);
    }
}
