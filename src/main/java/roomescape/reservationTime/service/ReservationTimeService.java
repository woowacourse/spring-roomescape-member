package roomescape.reservationTime.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.common.exception.DuplicateException;
import roomescape.common.exception.ForeignKeyException;
import roomescape.common.exception.InvalidIdException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.admin.ReservationTimeRequest;
import roomescape.reservationTime.dto.admin.ReservationTimeResponse;
import roomescape.reservationTime.dto.user.AvailableReservationTimeRequest;
import roomescape.reservationTime.dto.user.AvailableReservationTimeResponse;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;

@Service
public class ReservationTimeService {
    private static final String INVALID_THEME_ID_EXCEPTION_MESSAGE = "존재하지 않는 테마 아이디입니다";
    private static final String INVALID_TIME_ID_EXCEPTION_MESSAGE = "존재하지 않는 시간 아이디입니다";
    private static final String DUPLICATE_TIME_EXCEPTION_MESSAGE = "이미 해당 시간이 존재합니다";
    private static final String RESERVED_TIME_EXCEPTION_MESSAGE = "이미 예약된 시간은 삭제할 수 없습니다";

    private final Dao<ReservationTime> reservationTimeDao;
    private final Dao<Reservation> reservationDao;
    private final Dao<Theme> themeDao;

    public ReservationTimeService(
            Dao<ReservationTime> reservationTimeDao,
            Dao<Reservation> reservationDao,
            ThemeDao themeDao
    ) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(reservationTime -> new ReservationTimeResponse(
                        reservationTime.getId(),
                        reservationTime.getStartAt())
                )
                .toList();
    }

    public List<AvailableReservationTimeResponse> findByDateAndTheme(
            final AvailableReservationTimeRequest availableReservationTimeRequest
    ) {
        searchTheme(availableReservationTimeRequest);
        List<ReservationTime> reservedTimes = findReservedTimes(availableReservationTimeRequest);
        Set<Long> reservedIds = findReservedTimeIds(reservedTimes);

        List<ReservationTime> availableReservationTimes = reservationTimeDao.findAll();
        return availableReservationTimes.stream()
                .map(reservationTime -> new AvailableReservationTimeResponse(
                        reservationTime.getId(),
                        reservationTime.getStartAt(),
                        reservedIds.contains(reservationTime.getId())
                ))
                .toList();
    }

    private void searchTheme(
            final AvailableReservationTimeRequest availableReservationTimeRequest
    ) {
        themeDao.findById(availableReservationTimeRequest.themeId())
                .orElseThrow(() -> new InvalidIdException(INVALID_THEME_ID_EXCEPTION_MESSAGE));
    }

    private List<ReservationTime> findReservedTimes(
            final AvailableReservationTimeRequest availableReservationTimeRequest
    ) {
        return reservationDao.findAll().stream()
                .filter(reservation -> reservation.getDate().equals(availableReservationTimeRequest.date())
                        && reservation.getThemeId().equals(availableReservationTimeRequest.themeId())
                ).map(reservation -> new ReservationTime(
                        reservation.getTimeId(),
                        reservation.getTimeStartAt())
                )
                .toList();
    }

    private Set<Long> findReservedTimeIds(final List<ReservationTime> reservedTimes) {
        return reservedTimes.stream()
                .map(ReservationTime::getId)
                .collect(Collectors.toSet());
    }

    public ReservationTimeResponse add(final ReservationTimeRequest reservationTimeRequest) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        validateDuplicate(reservationTimeRequest, reservationTimes);

        ReservationTime newReservationTime = new ReservationTime(reservationTimeRequest.startAt());
        ReservationTime savedReservationTime = reservationTimeDao.add(newReservationTime);
        return new ReservationTimeResponse(savedReservationTime.getId(), savedReservationTime.getStartAt());
    }

    private void validateDuplicate(
            final ReservationTimeRequest reservationTimeRequest,
            final List<ReservationTime> reservationTimes
    ) {
        boolean isAlreadyExisted = reservationTimes.stream()
                .anyMatch(reservationTime -> reservationTime.getStartAt().equals(reservationTimeRequest.startAt()));

        if (isAlreadyExisted) {
            throw new DuplicateException(DUPLICATE_TIME_EXCEPTION_MESSAGE);
        }
    }

    public void deleteById(final Long id) {
        searchReservationTimeId(id);

        List<Reservation> reservations = reservationDao.findAll();

        validateUnoccupiedTime(id, reservations);
    }

    private void searchReservationTimeId(final Long id) {
        reservationTimeDao.findById(id)
                .orElseThrow(() -> new InvalidIdException(INVALID_TIME_ID_EXCEPTION_MESSAGE));
    }

    private void validateUnoccupiedTime(final Long id, final List<Reservation> reservations) {
        boolean isOccupiedTimeId = reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId().equals(id));

        if (isOccupiedTimeId) {
            throw new ForeignKeyException(RESERVED_TIME_EXCEPTION_MESSAGE);
        }
    }
}
