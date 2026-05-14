package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeStatusResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;
    private final Clock clock;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao, Clock clock) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
        this.clock = clock;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime save(ReservationTime reservationTime) {
        if(!reservationTimeDao.existsById(reservationTime.getId())){
            throw new IllegalArgumentException("시간 ID가 존재하지 않습니다.");
        }

        if (reservationTimeDao.existsByStartAt(reservationTime.getStartAt())) {
            throw new IllegalArgumentException("이미 존재하는 예약시간입니다.");
        }
        return reservationTimeDao.save(reservationTime);
    }

    public void deleteById(Long id) {
        validateHasTime(id);
        reservationTimeDao.deleteById(id);
    }

    public List<ReservationTimeStatusResponse> findReservationTimeByDateAndThemeId(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        List<Long> timeIds = reservationDao.findReservedTimeIdsByDateAndThemeId(date, themeId);

        LocalDateTime now = LocalDateTime.now(clock);
        return reservationTimes.stream()
                .map(reservationTime -> {
                    boolean available = !timeIds.contains(reservationTime.getId())
                            && !reservationTime.isPast(date, now);
                    return ReservationTimeStatusResponse.of(reservationTime, available);
                })
                .toList();
    }

    private void validateHasTime(Long id) {
        boolean hasReservation = reservationDao.existByTimeId(id);
        if (hasReservation) {
            throw new IllegalArgumentException("예약이 존재해 삭제할 수 없습니다");
        }
    }
}
