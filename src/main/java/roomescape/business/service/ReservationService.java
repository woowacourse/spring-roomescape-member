package roomescape.business.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;
import roomescape.exception.DuplicateException;
import roomescape.exception.InvalidReservationDateException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.persistence.dao.ReservationDao;
import roomescape.presentation.dto.ReservationAvailableTimeResponse;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;

@Service
public class ReservationService {

    private final PlayTimeService playTimeService;
    private final ThemeService themeService;
    private final ReservationDao reservationDao;

    public ReservationService(
            final PlayTimeService playTimeService,
            final ThemeService themeService,
            final ReservationDao reservationDao
    ) {
        this.playTimeService = playTimeService;
        this.themeService = themeService;
        this.reservationDao = reservationDao;
    }

    public ReservationResponse create(final ReservationRequest reservationRequest) {
        final PlayTime playTime = playTimeService.find(reservationRequest.timeId());
        final Theme theme = themeService.find(reservationRequest.themeId());
        validateIsDuplicate(reservationRequest.date(), playTime, theme);

        final Reservation reservation = reservationRequest.toDomain(playTime, theme);
        validateIsFuture(reservation);

        final Long id = reservationDao.save(reservation);

        return ReservationResponse.withId(reservation, id);
    }

    private void validateIsDuplicate(
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        if (reservationDao.existsByDateAndTimeAndTheme(date, playTime, theme)) {
            throw new DuplicateException("추가 하려는 예약과 같은 날짜, 시간, 테마의 예약이 이미 존재합니다.");
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
}
