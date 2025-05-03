package roomescape.reservationTime.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.AvailableReservationTimeRequest;
import roomescape.reservationTime.dto.AvailableReservationTimeResponse;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.theme.domain.Theme;

@Service
public class ReservationTimeService {
    private final Dao<ReservationTime> reservationTimeDao;
    private final Dao<Reservation> reservationDao;
    private final Dao<Theme> themeDao;

    public ReservationTimeService(
            Dao<ReservationTime> reservationTimeDao,
            Dao<Reservation> reservationDao,
            Dao<Theme> themeDao
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
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마 아이디입니다"));
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

        ReservationTime newReservationTime = new ReservationTime(null, reservationTimeRequest.startAt());
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
            throw new IllegalArgumentException("[ERROR] 이미 해당 시간이 존재합니다");
        }
    }

    public void deleteById(final Long id) {
        searchReservationTimeId(id);

        List<Reservation> reservations = reservationDao.findAll();

        validateUnoccupiedTime(id, reservations);
    }

    private void searchReservationTimeId(final Long id) {
        reservationTimeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 id의 시간이 존재하지 않습니다"));
    }

    private void validateUnoccupiedTime(final Long id, final List<Reservation> reservations) {
        boolean isOccupiedTimeId = reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId().equals(id));

        if (isOccupiedTimeId) {
            throw new IllegalArgumentException("[ERROR] 이미 예약된 시간은 삭제할 수 없습니다");
        }
    }
}
