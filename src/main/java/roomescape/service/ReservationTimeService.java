package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
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
        if (reservationTimeQueryingDao.existsByStartAt(reservationTimeReq.getStartAt())) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_ALREADY_EXISTS);
        }

        Long generatedId;
        try {
            generatedId = reservationTimeUpdatingDao.insert(reservationTimeReq);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_ALREADY_EXISTS);
        }

        return ReservationTimeResponse.from(new ReservationTime(generatedId, reservationTimeReq.getStartAt()));
    }

    public List<ReservationTimeResponse> read(LocalDate date, Long themeId) {
        if (date == null && themeId == null) {
            return reservationTimeQueryingDao.findAllReservationTime().stream()
                    .map(ReservationTimeResponse::from)
                    .toList();
        }
        return reservationTimeQueryingDao.findAvailableReservationTimes(date, themeId).stream()
                .map(ReservationTimeResponse::from)
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
        if (reservationQueryingDao.existsReservationByTimeId(id)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_DELETE_CONFLICT);
        }
        reservationTimeUpdatingDao.delete(id);
    }
}
