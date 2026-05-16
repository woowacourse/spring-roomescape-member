package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeUpdateRequest;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
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
    public ReservationTimeResponse create(ReservationTimeCreateRequest reservationTimeReq) {
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
    public ReservationTimeResponse update(Long id, ReservationTimeUpdateRequest newReservationTimeReq) {
        if (!reservationTimeQueryingDao.existsById(id)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND);
        }

        reservationTimeUpdatingDao.update(id, newReservationTimeReq);

        ReservationTime findReservationTime = reservationTimeQueryingDao.findReservationTimeById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND));

        return ReservationTimeResponse.from(findReservationTime);
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationTimeQueryingDao.existsById(id)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND);
        }

        if (reservationQueryingDao.existsReservationByTimeId(id)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_DELETE_CONFLICT);
        }
        reservationTimeUpdatingDao.delete(id);
    }
}
