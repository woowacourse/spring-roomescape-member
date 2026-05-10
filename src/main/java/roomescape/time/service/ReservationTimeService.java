package roomescape.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateResourceException;
import roomescape.exception.ResourceInUseException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTime save(ReservationTimeRequest request) {
        validateDuplicateTime(request);

        ReservationTime reservationTime = ReservationTime.create(request.startAt());
        return reservationTimeRepository.save(reservationTime);
    }

    @Transactional
    public void deleteById(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new ResourceInUseException("이 시간을 참조하는 예약이 있어 삭제할 수 없습니다. ID: " + id);
        }

        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> findAvailableTimes(Long themeId, LocalDate date) {
        return reservationTimeRepository.findAvailableTimes(themeId, date);
    }

    private void validateDuplicateTime(ReservationTimeRequest request) {
        if (reservationTimeRepository.existsByStartAt(request.startAt())) {
            throw new DuplicateResourceException("해당 시간이 이미 존재합니다.");
        }
    }

    public ReservationTime getById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 예약 시간이 존재하지 않습니다. ID: " + id));
    }
}
