package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRequest;
import roomescape.domain.reservationtime.ReservationTimeResponse;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.repository.ReservationQueryingDao;
import roomescape.repository.ReservationTimeQueryingDao;
import roomescape.repository.ReservationTimeUpdatingDao;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ReservationTimeService {

    private final ReservationTimeQueryingDao reservationTimeQueryingDao;
    private final ReservationTimeUpdatingDao reservationTimeUpdatingDao;
    private final ReservationQueryingDao reservationQueryingDao;

    public ReservationTimeService(ReservationTimeQueryingDao reservationTimeQueryingDao, ReservationTimeUpdatingDao reservationTimeUpdatingDao, ReservationQueryingDao reservationQueryingDao) {
        this.reservationTimeQueryingDao = reservationTimeQueryingDao;
        this.reservationTimeUpdatingDao = reservationTimeUpdatingDao;
        this.reservationQueryingDao = reservationQueryingDao;
    }

    @Transactional
    public ReservationTimeResponse create(ReservationTimeRequest reservationTimeReq) {
        Long generatedId = reservationTimeUpdatingDao.insert(reservationTimeReq);
        return ReservationTimeResponse.from(new ReservationTime(generatedId, reservationTimeReq.getStartAt()));
    }

    public List<ReservationTimeResponse> read(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeQueryingDao.findAllReservationTime(date, themeId);
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
    public void update(ReservationTimeRequest newReservationTimeReq, Long id) {
        reservationTimeUpdatingDao.update(id, newReservationTimeReq);
    }

    @Transactional
    public void delete(Long id) {
        ReservationTime findReservationTime = reservationTimeQueryingDao.findReservationTimeById(id)
                .orElseThrow(() -> new ReservationTimeNotFoundException(id));

        if (reservationQueryingDao.existsReservationByTimeId(findReservationTime.getId())) {
            throw new IllegalArgumentException("예약이 있는 시간은 삭제할 수 없습니다.");
        }
        reservationTimeUpdatingDao.delete(id);
    }
}
