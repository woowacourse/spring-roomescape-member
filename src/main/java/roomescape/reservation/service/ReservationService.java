package roomescape.reservation.service;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.dao.ThemeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.handler.exception.CustomBadRequest;
import roomescape.reservation.handler.exception.CustomException;
import roomescape.reservation.handler.exception.CustomInternalServerError;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = findReservationTime(reservationRequest);

        reservationDao.findAllReservations().stream()
                .filter(reservation ->
                        reservation.isSameTime(reservationTime) && reservation.isSameDate(reservationRequest.date()))
                .findAny()
                .ifPresent(time -> {
                    throw new CustomException(CustomBadRequest.DUPLICATE_RESERVATION);
                });

        Theme theme = themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new CustomException(CustomBadRequest.NOT_FOUND_THEME));

        Reservation reservation = reservationRequest.toEntity(reservationTime, theme);
        try {
            Reservation savedReservation = reservationDao.save(reservation);
            return ReservationResponse.from(savedReservation);
        } catch (DataAccessException e) {
            throw new CustomException(CustomInternalServerError.FAIl_TO_CREATE);
        }
    }

    private ReservationTime findReservationTime(ReservationRequest reservationRequest) {
        return reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new CustomException(CustomBadRequest.NOT_FOUND_RESERVATION_TIME));

    }

    public List<ReservationResponse> findAllReservations() {
        List<Reservation> reservations = reservationDao.findAllReservations();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteReservation(Long id) {
        try {
            reservationDao.delete(id);
        } catch (DataAccessException e) {
            throw new CustomException(CustomInternalServerError.FAIL_TO_REMOVE);
        }
    }
}
