package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeDeleteResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(
            final ReservationTimeDao reservationTimeDao,
            final ReservationDao reservationDao
    ) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeResponse save(final ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toEntity();
        return ReservationTimeResponse.from(reservationTimeDao.save(reservationTime));
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTimeResponse> reservationTimes = getAll();
        if (reservationTimes.isEmpty()) {
            throw new IllegalStateException("[ERROR] 방탈출 예약이 가능한 시간이 없습니다.");
        }
        return reservationTimes;
    }

    public List<ReservationTimeResponse> getAll() {
        return reservationTimeDao.getAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeDeleteResponse delete(final long id) {
        validateAlreadyHasReservation(id);
        return new ReservationTimeDeleteResponse(reservationTimeDao.delete(id));
    }

    private void validateAlreadyHasReservation(final long id) {
        List<Reservation> reservations = reservationDao.findByTimeId(id);
        if (!reservations.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }
    }
}
