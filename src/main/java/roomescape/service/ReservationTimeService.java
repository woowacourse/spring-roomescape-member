package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRequest;
import roomescape.domain.reservationtime.ReservationTimeResponse;
import roomescape.exception.ReservationNotFoundException;
import roomescape.repository.ReservationTimeQueryingDao;
import roomescape.repository.ReservationTimeUpdatingDao;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ReservationTimeService {

    private final ReservationTimeQueryingDao reservationTimeQueryingDao;
    private final ReservationTimeUpdatingDao reservationTimeUpdatingDao;

    public ReservationTimeService(ReservationTimeQueryingDao reservationTimeQueryingDao, ReservationTimeUpdatingDao reservationTimeUpdatingDao) {
        this.reservationTimeQueryingDao = reservationTimeQueryingDao;
        this.reservationTimeUpdatingDao = reservationTimeUpdatingDao;
    }

    public List<ReservationTimeResponse> read() {
        List<ReservationTime> reservationTimes = reservationTimeQueryingDao.findAllReservationTime();
        return reservationTimes.stream()
                .map(reservationTime -> ReservationTimeResponse.from(
                        new ReservationTime(
                                reservationTime.getId(),
                                reservationTime.getStartAt()
                        )
                ))
                .toList();
    }

    @Transactional
    public ReservationTimeResponse create(ReservationTimeRequest reservationTimeReq) {
        Long generatedId = reservationTimeUpdatingDao.insert(reservationTimeReq);
        return ReservationTimeResponse.from(new ReservationTime(generatedId, reservationTimeReq.getStartAt()));
    }

    @Transactional
    public void update(ReservationTimeRequest newReservationTimeReq, Long id) {
        reservationTimeUpdatingDao.save(id, newReservationTimeReq);
    }

    @Transactional
    public void delete(Long id) {
        int delete = reservationTimeUpdatingDao.delete(id);

        if (delete == 0) {
            throw new ReservationNotFoundException(id);
        }
    }
}
