package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.reservationTime.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeCreateResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeUserResponse;
import roomescape.exception.ExceptionCause;
import roomescape.exception.ReservationExistException;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeCreateResponse create(final ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTime reservationTime = reservationTimeDao.create(
                ReservationTime.create(reservationTimeCreateRequest.startAt()));
        return ReservationTimeCreateResponse.from(reservationTime);
    }

    public List<ReservationTimeUserResponse> findAllByDateAndTheme(final long themeId, final LocalDate date) {
        return reservationTimeDao.findAll().stream()
                .map(reservationTime ->
                        ReservationTimeUserResponse.from(
                                reservationTime,
                                reservationTimeDao.findByIdAndDateAndTheme(reservationTime.getId(), themeId, date)
                                        .isPresent()))
                .toList();
    }

    public void deleteIfNoReservation(final Long id) {
        ReservationTime reservationTime = findById(id);
        if (reservationDao.findByTimeId(id).isPresent()) {
            throw new ReservationExistException(ExceptionCause.RESERVATION_EXIST_TIME);
        }
        reservationTimeDao.delete(reservationTime);
    }

    public ReservationTime findById(final Long id) {
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(id);
        if (reservationTime.isEmpty()) {
            throw new NoSuchElementException("예약 시간이 존재하지 않습니다.");
        }
        return reservationTime.get();
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
