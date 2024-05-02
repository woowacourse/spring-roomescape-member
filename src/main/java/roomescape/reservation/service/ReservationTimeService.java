package roomescape.reservation.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.request.AvailableTimeRequest;
import roomescape.reservation.dto.request.ReservationTimeRequest;
import roomescape.reservation.dto.response.AvailableTimeResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse;
import roomescape.reservation.handler.exception.CustomException;
import roomescape.reservation.handler.exception.ExceptionCode;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toEntity();

        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAllReservationTimes();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableTimeResponse> findAvailableTimes(AvailableTimeRequest availableTimeRequest) {
        List<Reservation> reservations = reservationDao.findReservationsByDateAndThemeId(
                availableTimeRequest.date(),
                availableTimeRequest.themeId()
        );

        List<ReservationTime> reservationTimes = reservationTimeDao.findAllReservationTimes();

        return reservationTimes.stream()
                .map(reservationTime -> new AvailableTimeResponse(
                        reservationTime.getStartAt(),
                        reservationTime.getId(),
                        isReservedTime(reservations, reservationTime)
                ))
                .toList();
    }

    private boolean isReservedTime(List<Reservation> reservations, ReservationTime reservationTime) {
        return reservations.stream()
                .noneMatch(reservation -> reservation.isSameTime(reservationTime));
    }

    public void deleteReservationTime(Long id) {
        try {
            reservationTimeDao.delete(id);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ExceptionCode.TIME_IN_USE);
        }
    }
}
