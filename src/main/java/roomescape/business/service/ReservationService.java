package roomescape.business.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Role;
import roomescape.business.domain.Theme;
import roomescape.business.domain.User;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidReservationDateException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.auth.UnauthorizedAccessException;
import roomescape.persistence.dao.ReservationDao;
import roomescape.presentation.dto.reservation.ReservationAvailableTimeResponse;
import roomescape.presentation.dto.reservation.ReservationFilterDto;
import roomescape.presentation.dto.reservation.ReservationRequest;
import roomescape.presentation.dto.reservation.ReservationResponse;

@Service
public class ReservationService {

    private final UserService userService;
    private final PlayTimeService playTimeService;
    private final ThemeService themeService;

    private final ReservationDao reservationDao;

    public ReservationService(
            final UserService userService,
            final PlayTimeService playTimeService,
            final ThemeService themeService,
            final ReservationDao reservationDao
    ) {
        this.userService = userService;
        this.playTimeService = playTimeService;
        this.themeService = themeService;
        this.reservationDao = reservationDao;
    }

    public ReservationResponse create(
            final Long userId,
            final ReservationRequest reservationRequest
    ) {
        final User user = userService.find(userId);
        validateUserPermission(user.getRole());
        final PlayTime playTime = playTimeService.find(reservationRequest.timeId());
        final Theme theme = themeService.find(reservationRequest.themeId());
        validateIsDuplicate(reservationRequest.date(), playTime, theme);

        final Reservation reservation = reservationRequest.toDomain(user, playTime, theme);
        validateIsFuture(reservation);

        final Long id = reservationDao.save(reservation);

        return ReservationResponse.withId(reservation, id);
    }

    private void validateUserPermission(final Role role) {
        if (!role.hasPermission(Role.USER)) {
            throw new UnauthorizedAccessException(role, Role.USER);
        }
    }

    private void validateIsDuplicate(
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        if (reservationDao.existsByDateAndTimeAndTheme(date, playTime, theme)) {
            throw new DuplicateReservationException(date, playTime, theme);
        }
    }

    private void validateIsFuture(final Reservation reservation) {
        final LocalDateTime now = LocalDateTime.now();

        if (reservation.isBefore(now)) {
            throw new InvalidReservationDateException();
        }
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void remove(final Long id) {
        if (!reservationDao.remove(id)) {
            throw new ReservationNotFoundException(id);
        }
    }

    public List<ReservationAvailableTimeResponse> findAvailableTimes(
            final LocalDate date,
            final Long themeId
    ) {
        final Theme theme = themeService.find(themeId);

        return reservationDao.findAvailableTimesByDateAndTheme(date, theme);
    }

    public List<ReservationResponse> findByFilter(final ReservationFilterDto filter) {
        return reservationDao.findByFilter(filter).stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
