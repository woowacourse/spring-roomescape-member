package roomescape.reservation.service;

import java.util.List;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.AvailableTimeResponse;
import roomescape.handler.exception.CustomBadRequest;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.CustomInternalServerError;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final Clock clock;

    public ReservationService(ReservationDao reservationDao, ReservationTimeService reservationTimeService,
                              ThemeService themeService, Clock clock) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.clock = clock;
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
                .filter(r -> r.isSameDateTime(reservation))
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
                        isReservable(reservedTimes, reservationTime) && !isPast(date, reservationTime)
                ))
                .toList();
    }

    private boolean isReservable(List<ReservationTime> reservedTimes, ReservationTime reservationTime) {
        return reservedTimes.stream()
                .noneMatch(reservedTime -> reservedTime.equals(reservationTime));
    }

    private boolean isPast(LocalDate date, ReservationTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, reservationTime.getStartAt());
        return reservationDateTime.isBefore(LocalDateTime.now(clock));
    }

    public void deleteReservation(Long id) {
        try {
            reservationDao.delete(id);
        } catch (DataAccessException e) {
            throw new CustomException(CustomInternalServerError.FAIL_TO_REMOVE);
        }
    }
}
