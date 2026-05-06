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
import roomescape.dto.response.CreateReservationTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;

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

    public CreateReservationTimeResponse addReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = request.toReservationTime();
        ReservationTime newReservationTime = reservationTimeDao.insert(reservationTime);
        return CreateReservationTimeResponse.from(newReservationTime);
    }

    // TODO: 지난 날짜 검증
    public List<ReservationTimeResponse> getReservationTimes(Long themeId, LocalDate date) {
        validateTheme(themeId);

        List<Reservation> reservations = reservationDao.selectByThemeIdAndDate(themeId, date);
        List<ReservationTime> reservationTimes = reservationTimeDao.selectAll();

        Set<Long> reservedTimeIds = extractReservedTimeIds(reservations);
        return reservationTimes.stream()
                .map(time -> ReservationTimeResponse.from(time, isNotReserved(time, reservedTimeIds)))
                .toList();
    }

    private void validateTheme(Long themeId) {
        boolean exists = themeDao.existsById(themeId);
        if (!exists) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
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

    public void deleteReservationTime(long reservationTimeId) {
        validateReservationTimeExists(reservationTimeId);
        reservationTimeDao.delete(reservationTimeId);
    }

    private void validateReservationTimeExists(Long reservationTimeId) {
        boolean exists = reservationTimeDao.existsById(reservationTimeId);
        if (!exists) {
            throw new IllegalArgumentException("존재하지 않는 시간입니다.");
        }
    }
}
