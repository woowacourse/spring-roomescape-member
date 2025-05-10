package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.exception.NotFoundException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationCreateResponse;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(final ReservationDao reservationDao,
                              final ReservationTimeService reservationTimeService, final ThemeService themeService) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public ReservationCreateResponse create(ReservationCreateRequest reservationCreateRequest) {
        ReservationTime time = reservationTimeService.findById(reservationCreateRequest.timeId());
        Theme theme = themeService.findById(reservationCreateRequest.themeId());
        Reservation reservation = Reservation.create(
                reservationCreateRequest.name(),
                reservationCreateRequest.date(),
                time,
                theme);

        if (reservationDao.findByThemeAndDateAndTime(reservation).isPresent()) {
            throw new ConflictException(ExceptionCause.RESERVATION_DUPLICATE);
        }
        return ReservationCreateResponse.from(reservationDao.create(reservation));
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        ReservationTimeResponse.from(reservation.getTime()),
                        ThemeResponse.from(reservation.getTheme())
                ))
                .toList();
    }

    public void delete(final Long id) {
        if (!reservationDao.existById(id)) {
            throw new NotFoundException(ExceptionCause.RESERVATION_NOTFOUND);
        }
        reservationDao.delete(id);
    }
}
