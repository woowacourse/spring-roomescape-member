package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao,
                                  ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    @Transactional
    public ReservationTimeResponse create(ReservationTime reservationTime) {
        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse findById(Long id) {
        ReservationTime reservationTime = reservationTimeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 ID의 예약 시간이 없습니다."));
        return ReservationTimeResponse.from(reservationTime);
    }

    @Transactional
    public void delete(Long id) {
        ReservationTime reservationTime = reservationTimeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 ID의 예약 시간이 없습니다."));
        validateHasReservation(reservationTime);
        reservationTimeDao.deleteById(reservationTime.getId());
    }

    private void validateHasReservation(ReservationTime reservationTime) {
        int reservationCount = reservationDao.countByTimeId(reservationTime.getId());
        if (reservationCount > 0) {
            throw new IllegalArgumentException("해당 예약 시간의 예약 건이 존재합니다.");
        }
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTimes(LocalDate date, Long themeId) {
        List<Long> reservations = reservationDao.findAllTimeIdsByDateAndThemeId(date, themeId);
        Set<Long> reservedTimeIds = new HashSet<>(reservations);
        List<ReservationTime> times = reservationTimeDao.findAll();

        return times.stream()
                .map(reservationTime -> {
                    boolean isReserved = reservedTimeIds.contains(reservationTime.getId());
                    return AvailableReservationTimeResponse.of(reservationTime, isReserved);
                })
                .collect(Collectors.toList());
    }
}
