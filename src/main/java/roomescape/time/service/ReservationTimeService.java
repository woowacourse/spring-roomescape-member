package roomescape.time.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Slf4j
@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    @Transactional
    public ReservationTime create(LocalTime startAt) {
        validateDuplicateTimeExist(startAt);
        ReservationTime reservationTime = reservationTimeRepository.save(ReservationTime.create(startAt));
        log.info("Reservation time created: id={}, startAt={}", reservationTime.id(), startAt);
        return reservationTime;
    }

    private void validateDuplicateTimeExist(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            log.warn("Reservation time already exists: startAt={}", startAt);
            throw new ConflictException("이미 존재하는 예약 시간입니다.");
        }
    }

    @Transactional
    public void delete(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reservation time not found: id={}", id);
                    return new NotFoundException("존재하지 않는 예약 시간입니다.");
                });
        reservationTimeRepository.delete(id);
        log.info("Reservation time deleted: id={}, startAt={}", id, reservationTime.startAt());
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> readAvailableTimes(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailableByDateAndThemeId(date, themeId);
    }
}
