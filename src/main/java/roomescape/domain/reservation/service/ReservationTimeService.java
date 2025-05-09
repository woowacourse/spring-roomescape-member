package roomescape.domain.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.common.exception.AlreadyInUseException;
import roomescape.domain.reservation.dto.ReservationTimeRequest;
import roomescape.domain.reservation.dto.ReservationTimeResponse;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationDao;
import roomescape.domain.reservation.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao, final ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTimeResponse> getAll() {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();

        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse create(final ReservationTimeRequest request) {
        ReservationTime reservationTime = request.toEntity();

        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public void delete(final Long id) {
        if (reservationDao.existsByTimeId(id)) {
            throw new AlreadyInUseException("Reservation is already in use");
        }

        reservationTimeDao.deleteById(id);
    }
}
