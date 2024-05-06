package roomescape.reservation.service;

import java.util.List;
import java.time.LocalDate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.AvailableTimeResponse;
import roomescape.reservation.handler.exception.CustomBadRequest;
import roomescape.reservation.handler.exception.CustomException;
import roomescape.reservation.handler.exception.CustomInternalServerError;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(ReservationDao reservationDao, ReservationTimeService reservationTimeService,
                              ThemeService themeService) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public Reservation createReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeService.findReservationTime(reservationRequest.timeId());
        Theme theme = themeService.findTheme(reservationRequest.themeId());
        Reservation reservation = reservationRequest.toEntity(reservationTime, theme);

        validateDuplication(reservation);

        try {
            return reservationDao.save(reservation);
        } catch (DataAccessException e) {
            throw new CustomException(CustomInternalServerError.FAIl_TO_CREATE);
        }
    }

    private void validateDuplication(Reservation reservation) {
        reservationDao.findAllReservations().stream()
                .filter(r -> r.isSameDateTIme(reservation))
                .findAny()
                .ifPresent(r -> {
                    throw new CustomException(CustomBadRequest.DUPLICATE_RESERVATION);
                });
    }

    public List<Reservation> findReservations(LocalDate date, Long themeId) {
        return reservationDao.findReservationsByDateAndThemeId(date, themeId);
    }

    public List<Reservation> findAllReservations() {
        return reservationDao.findAllReservations();
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, Long themeId) {
        List<ReservationTime> reservedTimes = findReservations(date, themeId).stream()
                .map(Reservation::getTime)
                .toList();
        List<ReservationTime> reservationTimes = reservationTimeService.findAllReservationTimes();

        return reservationTimes.stream()
                .map(reservationTime -> new AvailableTimeResponse(
                        reservationTime.getStartAt(),
                        reservationTime.getId(),
                        isReservedTime(reservedTimes, reservationTime)
                ))
                .toList();
    }

    private boolean isReservedTime(List<ReservationTime> reservedTimes, ReservationTime reservationTime) {
        return reservedTimes.stream()
                .noneMatch(reservedTime -> reservedTime.equals(reservationTime));
    }

    public void deleteReservation(Long id) {
        try {
            reservationDao.delete(id);
        } catch (DataAccessException e) {
            throw new CustomException(CustomInternalServerError.FAIL_TO_REMOVE);
        }
    }
}
