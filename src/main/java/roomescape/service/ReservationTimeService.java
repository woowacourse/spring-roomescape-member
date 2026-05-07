package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.code.ReservationErrorCode;
import roomescape.exception.code.ReservationTimeErrorCode;
import roomescape.exception.code.ThemeErrorCode;
import roomescape.exception.domain.ReservationException;
import roomescape.exception.domain.ReservationTimeException;
import roomescape.exception.domain.ThemeException;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ThemeDao themeDao,
                                  ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeResponse create(ReservationTimeRequest request) {
        ReservationTime reservationTime = request.toReservationTime();
        ReservationTime newReservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.from(newReservationTime);
    }

    public List<AvailableReservationTimeResponse> getReservationTimes(long themeId, LocalDate date) {
        validateTheme(themeId);

        List<Reservation> reservations = reservationDao.findByThemeIdAndDate(themeId, date);
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();

        Set<Long> reservedTimeIds = extractReservedTimeIds(reservations);
        return reservationTimes.stream()
                .map(time -> AvailableReservationTimeResponse.from(time, isNotReserved(time, reservedTimeIds)))
                .toList();
    }

    private void validateTheme(long themeId) {
        boolean exists = themeDao.existsById(themeId);
        if (!exists) {
            throw new ThemeException(ThemeErrorCode.THEME_NOT_FOUND);
        }
    }

    private Set<Long> extractReservedTimeIds(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> reservation.getTime().getId())
                .collect(Collectors.toSet());
    }

    private boolean isNotReserved(ReservationTime time, Set<Long> reservedTimeIds) {
        return !reservedTimeIds.contains(time.getId());
    }

    public void delete(long reservationTimeId) {
        validateReservationNotExistsBy(reservationTimeId);
        int affectedRows = reservationTimeDao.delete(reservationTimeId);

        if (affectedRows == 0) {
            throw new ReservationTimeException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND);
        }
    }

    private void validateReservationNotExistsBy(long reservationTimeId) {
        if (reservationDao.existsByReservationTime(reservationTimeId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_EXISTS);
        }
    }
}
