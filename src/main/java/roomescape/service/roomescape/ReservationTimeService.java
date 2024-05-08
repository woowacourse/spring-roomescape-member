package roomescape.service.roomescape;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.roomescape.Reservation;
import roomescape.domain.roomescape.ReservationTime;
import roomescape.dto.request.ReservationTimeSaveRequest;
import roomescape.dto.response.ReservationTimeDeleteResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.repository.roomescape.ReservationDao;
import roomescape.repository.roomescape.ReservationTimeDao;

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

    public ReservationTimeResponse save(final ReservationTimeSaveRequest reservationTimeSaveRequest) {
        ReservationTime reservationTime = reservationTimeSaveRequest.toEntity();
        return ReservationTimeResponse.from(reservationTimeDao.save(reservationTime));
    }

    public List<ReservationTimeResponse> getAll() {
        return reservationTimeDao.getAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeDeleteResponse delete(final long id) {
        validateDoesNotExists(id);
        validateAlreadyHasReservation(id);
        return new ReservationTimeDeleteResponse(reservationTimeDao.delete(id));
    }

    private void validateDoesNotExists(final long id) {
        if (reservationTimeDao.findById(id).isEmpty()) {
            throw new NoSuchElementException("[ERROR] (id : " + id + ") 에 대한 자원에 존재하지 않습니다.");
        }
    }

    private void validateAlreadyHasReservation(final long id) {
        List<Reservation> reservations = reservationDao.findByTimeId(id);
        if (!reservations.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }
    }
}
