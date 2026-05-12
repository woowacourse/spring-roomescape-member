package roomescape.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.global.exception.BusinessException;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTime saveTime(LocalTime startAt) {
        ReservationTime reservationTime = ReservationTime.create(startAt);
        return reservationTimeRepository.save(reservationTime);
    }

    public List<ReservationTime> getTimes() {
        return reservationTimeRepository.findAll();
    }

    public Set<Long> getBookedTimes(LocalDate date, Long themeId) {
        if (date == null || themeId == null) {
            return Set.of();
        }
        List<ReservationTime> bookedTimes = reservationRepository.findByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::getTime)
                .toList();

        return bookedTimes.stream()
                .map(ReservationTime::getId)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void deleteTime(Long id) {
        if (reservationRepository.existsByReservationTimeId(id)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "참조하고 있는 예약 시간이어서 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
