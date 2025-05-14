package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.exception.NotFoundException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.ReservationTimeCreateResponse;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeUserResponse;

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
        //TODO : 매번 조회하지 않도록 수정
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
            throw new ConflictException(ExceptionCause.RESERVATION_EXIST_TIME);
        }
        reservationTimeDao.delete(reservationTime);
    }

    public ReservationTime findById(final Long id) {
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(id);
        if (reservationTime.isEmpty()) {
            throw new NotFoundException(ExceptionCause.RESERVATION_TIME_NOTFOUND);
        }
        return reservationTime.get();
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
