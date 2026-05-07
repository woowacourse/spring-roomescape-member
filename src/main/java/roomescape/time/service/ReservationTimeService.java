package roomescape.time.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.request.ReservationTimeSaveDto;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> readAvailableTimes(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailableByDateAndThemeId(date, themeId);
    }

    @Transactional
    public ReservationTime register(ReservationTimeSaveDto dto) {
        validateDuplicateTimeExist(dto.startAt());
        return reservationTimeRepository.save(ReservationTime.create(dto.startAt()));
    }

    @Transactional
    public ReservationTime delete(Long id) {
        ReservationTime reservationTime = getReservationTime(id);
        reservationTimeRepository.delete(reservationTime.id());
        return reservationTime;
    }

    private void validateDuplicateTimeExist(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new ConflictException("이미 존재하는 예약 시간입니다.");
        }
    }

    private ReservationTime getReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));
    }

}
