package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.code.ReservationTimeErrorCode;
import roomescape.exception.code.ThemeErrorCode;
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

        List<ReservationTime> reservedTimes = reservationTimeDao.findByThemeIdAndDate(themeId, date);
        List<ReservationTime> allTimes = reservationTimeDao.findAll();

        return allTimes.stream()
                .map(time -> AvailableReservationTimeResponse.from(time, isReserved(time, reservedTimes)))
                .toList();
    }

    private void validateTheme(long themeId) {
        boolean exists = themeDao.existsById(themeId);
        if (!exists) {
            throw new ThemeException(ThemeErrorCode.THEME_NOT_FOUND);
        }
    }

    private boolean isReserved(ReservationTime time, List<ReservationTime> reservedTimes) {
        return reservedTimes.contains(time);
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
            throw new ReservationTimeException(ReservationTimeErrorCode.RESERVATION_TIME_HAS_RESERVATION);
        }
    }
}
