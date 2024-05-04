package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

import java.util.List;

@Service
class ReservationTimeServiceImpl implements ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeServiceImpl(
            final ReservationTimeDao reservationTimeDao,
            final ReservationDao reservationDao
    ) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    @Override
    public ReservationTimeResponse save(final ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toEntity();
        return new ReservationTimeResponse(reservationTimeDao.save(reservationTime));
    }

    @Override
    public List<ReservationTimeResponse> findAll() {
        return ReservationTimeResponse.listOf(reservationTimeDao.getAll());
    }

    @Override
    public void delete(final long id) {
        validateAlreadyHasReservation(id);
        validateIdExists(id);
        reservationTimeDao.delete(id);
    }

    private void validateAlreadyHasReservation(final long id) {
        List<Reservation> reservationsByTimeId = reservationDao.findByTimeId(id);
        if (!reservationsByTimeId.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }
    }

    private void validateIdExists(final long id) {
        reservationTimeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 삭제할 예약 시간이 없습니다."));
    }
}
